package searchengine.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.LemmaRepository;
import searchengine.utils.Lemmatizer;
import searchengine.utils.urlParsingTask;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

@Service
public class SiteIndexingService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final SitesList sitesList;
    private final LemmaService lemmaService;
    private final SearchIndexService searchIndexService;
    private final Lemmatizer lemmatizer = Lemmatizer.getInstance();

    private static boolean isIndexing = false;

    @Autowired
    public SiteIndexingService(SiteRepository siteRepository, PageRepository pageRepository,
                               LemmaRepository lemmaRepository, SitesList sitesList,
                               LemmaService lemmaService, SearchIndexService searchIndexService) throws IOException {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.lemmaRepository = lemmaRepository;
        this.sitesList = sitesList;
        this.lemmaService = lemmaService;
        this.searchIndexService = searchIndexService;
    }

    public void startFullIndexing() {
        isIndexing = true;
        for (Site site : getSitesFromConfig()) {
            deleteExistingDataBySite(site);
            site.setStatus("INDEXING");
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
            parseSite(site);
            if (site.getStatus().equals("INDEXING")) {
                site.setStatus("INDEXED");
            }
            siteRepository.save(site);
        }
        isIndexing = false;
    }

    public void stopIndexing() {
        if (!getSitesFromConfig().isEmpty()) {
            for (Site site : getSitesFromConfig()) {
                if (siteRepository.findByUrl(site.getUrl()).isPresent() && isIndexing()) {
                    if (siteRepository.findByUrl(site.getUrl()).get().getStatus().equals("INDEXING")) {
                        siteRepository.findByUrl(site.getUrl()).get().setStatus("FAILED");
                        siteRepository.findByUrl(site.getUrl()).get().setLastError("Ошибка индексации");
                        siteRepository.save(siteRepository.findByUrl(site.getUrl()).get());
                    }
                    isIndexing = false;
                }
            }
        }
    }

    public List<Site> getSitesFromConfig() {
        List<searchengine.config.Site> sites = sitesList.getSites();
        List<Site> parsedSites = new ArrayList<>();
        for (int i = 0; i < sites.size(); i++) {
            parsedSites.add(new Site());
            parsedSites.get(i).setName(sites.get(i).getName());
            parsedSites.get(i).setUrl(sites.get(i).getUrl());
        }
        return parsedSites;
    }


    public void savePageToDB(String siteUrl, String path, int code, String content) {
        Site site = siteRepository.findByUrl(siteUrl).orElseGet(() -> {
            Site newSite = new Site();
            newSite.setUrl(siteUrl);
            return siteRepository.save(newSite);
        });

        Page page = new Page();
        page.setSite(site);
        page.setPath(path);
        page.setCode(code);
        page.setContent(content);

        pageRepository.save(page);
    }

    private void deleteExistingDataBySite(Site site) {
        pageRepository.deleteBySiteId(site.getId());
        siteRepository.deleteByUrl(site.getUrl());
    }

    private void parseSite(Site site) {
        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            ConcurrentHashMap<String, String> pageMap = new ConcurrentHashMap<>();
            TreeSet<String> set = new TreeSet<>();
            urlParsingTask urlParsingTask = new urlParsingTask(site.getUrl(), pageMap, set);
            forkJoinPool.invoke(urlParsingTask);

            for (String url : set) {
                String content = pageMap.get(url);
                savePageToDB(site.getUrl(), url, 200, content);
                site.setStatusTime(LocalDateTime.now());
                if (content != null) {
                    HashMap<String, Integer> lemmaMap = lemmatizer.lemmatize(content);
                    for (Map.Entry<String, Integer> entry : lemmaMap.entrySet()) {
                        lemmaService.saveLemmaToDB(entry.getValue(), entry.getKey(), pageRepository.getByPath(url).getSite());
                        searchIndexService.saveSearchIndexToDB(
                                pageRepository.getByPath(url).getId(),
                                lemmaRepository.getByLemma(entry.getKey()).getId(),
                                lemmaRepository.getByLemma(entry.getKey()).getFrequency()
                        );
                    }
                }
            }
            System.out.println("Добавлено " + set.size() + " уникальных ссылок для сайта " + site.getName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during indexing: " + e.getMessage());
        }
    }

    public Optional<Site> findById(Integer id) {
        return Optional.ofNullable(siteRepository.findById(id));
    }

    public Site findByName(String url) {
        return siteRepository.findByUrlLike(url);
    }

    public static boolean isIndexing() {
        return isIndexing;
    }
}

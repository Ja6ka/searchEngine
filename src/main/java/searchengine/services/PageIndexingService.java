package searchengine.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.utils.Lemmatizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PageIndexingService {
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final SiteIndexingService siteIndexingService;
    private final LemmaService lemmaService;
    private final SearchIndexService searchIndexService;
    private final StatisticsService statisticsService;
    private final Lemmatizer lemmatizer = Lemmatizer.getInstance();

    @Autowired
    public PageIndexingService(PageRepository pageRepository, LemmaRepository lemmaRepository,
                               SiteIndexingService siteIndexingService, LemmaService lemmaService,
                               SearchIndexService searchIndexService, StatisticsService statisticsService) throws IOException {
        this.pageRepository = pageRepository;
        this.lemmaRepository = lemmaRepository;
        this.siteIndexingService = siteIndexingService;
        this.lemmaService = lemmaService;
        this.searchIndexService = searchIndexService;
        this.statisticsService = statisticsService;
    }

    public ResponseEntity reIndexPage(String url) throws IOException {
        if (isValid(url)) {
        pageRepository.deleteByPath(url);
        StringBuilder builder = new StringBuilder();
        String[] arr = url.split("/");
        String siteUrl = builder.append(arr[0]).append("//").append(arr[2]).toString();
        Document doc = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com").get();
        siteIndexingService.savePageToDB(siteUrl, url, 200, doc.text());
        HashMap<String, Integer> map = lemmatizer.lemmatize(doc.text());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            lemmaService.saveLemmaToDB(entry.getValue(), entry.getKey(), pageRepository.getByPath(url).getSite());
            searchIndexService.saveSearchIndexToDB(
                    pageRepository.getByPath(url).getId(),
                    lemmaRepository.getByLemma(entry.getKey()).getId(),
                    lemmaRepository.getByLemma(entry.getKey()).getFrequency()
            );
        }
        return ResponseEntity.ok(statisticsService.getStatistics());
        } else {
            return new ResponseEntity(ResponseEntity.noContent(), HttpStatus.FORBIDDEN);
        }
    }

    private boolean isValid(String url) {
        boolean isValid = false;
        if (url.startsWith("https://") || url.startsWith("http://")) {
            StringBuilder builder = new StringBuilder();
            List<Site> sitesList = siteIndexingService.getSitesFromConfig();
            String[] arr = url.split("/");
            String siteUrl = builder.append(arr[0]).append("//").append(arr[2]).toString();
            for (Site site : sitesList) {
                if (site.getUrl().equals(siteUrl) || site.getUrl().equals(siteUrl + "/")) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    public Optional<Page> findPage(long id) {
        return pageRepository.findById(id);
    }

    public Long countPages() {
        return pageRepository.count();
    }
}

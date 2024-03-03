package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final SitesList sites;
    @Autowired
    private final PageRepository pageRepository;

    @Autowired
    private final SiteRepository siteRepository;
    @Autowired
    private final LemmaRepository lemmaRepository;

    @Override
    public StatisticsResponse getStatistics() throws NoSuchElementException {

        TotalStatistics total = new TotalStatistics();
        total.setSites(sites.getSites().size());
        total.setIndexing(true);

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<Site> sitesList = sites.getSites();
        for (int i = 0; i < sitesList.size(); i++) {
            int pages = 0;
            int lemmas = 0;
            Site site = sitesList.get(i);
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            if (siteRepository.findByUrl(site.getUrl()).isPresent()) {
                pages = pageRepository.countBySiteId(siteRepository.findByUrl(site.getUrl()).get().getId());
                lemmas = lemmaRepository.countBySiteId(siteRepository.findByUrl(site.getUrl()).get().getId());
                item.setError(siteRepository.findByUrl(site.getUrl()).get().getLastError());
                item.setStatusTime((siteRepository.findByUrl(site.getUrl()).get().getStatusTime().toEpochSecond(ZoneOffset.UTC) - 3 * 3600) * 1000);
                item.setStatus(siteRepository.findByUrl(site.getUrl()).get().getStatus());
            } else {
                item.setError("Сайт не проиндексирован");
            }
            item.setPages(pages);
            item.setLemmas(lemmas);
            total.setPages(total.getPages() + pages);
            total.setLemmas(total.getLemmas() + lemmas);
            detailed.add(item);
        }

        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }
}

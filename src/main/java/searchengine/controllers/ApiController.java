package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.responses.ErrorResponse;
import searchengine.dto.statistics.responses.SearchResponse;
import searchengine.model.search.Data;
import searchengine.services.PageIndexingService;
import searchengine.services.SearcherService;
import searchengine.services.SiteIndexingService;
import searchengine.services.StatisticsService;
import searchengine.utils.urlParsingTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statisticsService;
    private final PageIndexingService pageIndexingService;

    private final SiteIndexingService siteIndexingService;

    private final SearcherService searcherService;

    @Autowired
    public ApiController(StatisticsService statisticsService, PageIndexingService pageIndexingService, SiteIndexingService siteIndexingService, SearcherService searcherService) {
        this.statisticsService = statisticsService;
        this.pageIndexingService = pageIndexingService;
        this.siteIndexingService = siteIndexingService;
        this.searcherService = searcherService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @PostMapping("/indexPage")
    public ResponseEntity indexPage(@RequestParam(required = false) String url) throws IOException {

        return pageIndexingService.reIndexPage(url);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "site", required = false, defaultValue = "") String site,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "0") int limit)
            throws IOException {
        if (query.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(false, "Задан пустой поисковый запрос!");
            return ResponseEntity.ok(errorResponse);
        } else {
            ArrayList<Data> dataArray = searcherService.getDataFromSearchInput(query, site);
            SearchResponse searchResponse = new SearchResponse(true, dataArray.size(), dataArray);
            return ResponseEntity.ok(searchResponse);
        }
    }

    @GetMapping("/startIndexing")
    @ResponseBody
    public Map<String, Object> indexAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            urlParsingTask.setStopping(false);
            siteIndexingService.startFullIndexing();
            response.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/stopIndexing")
    @ResponseBody
    public Map<String, Object> stopIndexing() {
        siteIndexingService.stopIndexing();
        Map<String, Object> response = new HashMap<>();
        response.put("result", true);
        return response;
    }
}

package searchengine.services;

import searchengine.model.search.Data;
import searchengine.utils.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SearcherService {

    private Searcher searcher;
    private SearchIndexService searchIndexService;

    @Autowired
    public SearcherService(Searcher searcher, SearchIndexService searchIndexService) {
        this.searcher = searcher;
        this.searchIndexService = searchIndexService;
    }

    public SearcherService() {
    }

    public ArrayList<Data> getDataFromSearchInput(String searchInput, String siteURL){
        ArrayList<Data> dataArrayList = searcher.searchStringToDataArray(searchInput, siteURL);
        return dataArrayList;
    }
}
package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import searchengine.model.SearchIndex;
import searchengine.repositories.SearchIndexRepository;

import java.util.List;

@Service
public class SearchIndexService {

    private final SearchIndexRepository searchIndexRepository;

    @Autowired
    public SearchIndexService(SearchIndexRepository searchIndexRepository) {
        this.searchIndexRepository = searchIndexRepository;
    }

    public void saveSearchIndexToDB(int pageId, int lemmaId, int frequency) {
        SearchIndex newIndex = new SearchIndex();
        newIndex.setPageId(pageId);
        newIndex.setLemmaId(lemmaId);
        newIndex.setSearchIndexRank(frequency);
        searchIndexRepository.save(newIndex);
    }

    public Boolean checkIfIndexExists(int page_id, int lemma_id) {
        return searchIndexRepository.existsByPageIdAndLemmaId(page_id, lemma_id);
    }

    public List<SearchIndex> findAllByLemmaId(int lemmaId, Limit limit) {
        return searchIndexRepository.findAllByLemmaId(lemmaId, limit);
    }

    public SearchIndex findByPageIdAndLemmaId(int pageId, int lemmaId) {
        return searchIndexRepository.findByPageIdAndLemmaId(pageId, lemmaId);
    }
}

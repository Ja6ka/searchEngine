package searchengine.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.SearchIndex;

import java.util.List;

@Repository
public interface SearchIndexRepository extends JpaRepository<SearchIndex, Long> {

    @Transactional
    void deleteByPageId(Integer pageId);

    @Transactional
    boolean existsByPageId(Integer pageId);

    @Transactional
    boolean existsByPageIdAndLemmaId(int pageId, int lemmaId);

    @Transactional
    List<SearchIndex> findAllByLemmaId(int lemmaId, Limit limit);

    @Transactional
    SearchIndex findByPageIdAndLemmaId(int pageId, int lemmaId);
}
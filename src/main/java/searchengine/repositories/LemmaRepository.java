package searchengine.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;

import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Long> {

    @Transactional
    void deleteBySiteId(Integer siteId);

    @Transactional
    Lemma getByLemma(String lemma);

    @Transactional
    boolean existsByLemma(String lemma);

    @Transactional
    Integer countBySiteId(Integer siteId);

    @Transactional
    Lemma findByLemmaAndSiteId(String lemma, Integer siteId);

    @Transactional
    List<Lemma> findAllByLemma(String lemma);
    @Transactional
    List<Lemma> findByLemma(String lemma);
}
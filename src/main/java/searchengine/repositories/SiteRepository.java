package searchengine.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Site;

import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Transactional
    Optional<Site> findByUrl(String url);

    @Transactional
    Site findByUrlLike(String url);

    @Transactional
    Site findById(int siteId);

    @Transactional
    void deleteByUrl(String url);


}

package searchengine.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Transactional
    void deleteBySiteId(Integer siteId);

    @Transactional
    Page getByPath(String path);

    @Transactional
    void deleteByPath(String path);

    @Transactional
    Integer countBySiteId(Integer siteId);
}

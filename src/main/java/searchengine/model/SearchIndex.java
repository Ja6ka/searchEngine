package searchengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class SearchIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT", nullable = false)
    private int id;

    @Column(columnDefinition = "INT", nullable = false, name = "page_id")
    private int pageId;

    @Column(columnDefinition = "INT", nullable = false, name = "lemma_id")
    private int lemmaId;

    @Column(columnDefinition = "FLOAT", nullable = false)
    private float searchIndexRank;

}

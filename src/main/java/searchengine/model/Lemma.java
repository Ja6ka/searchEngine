package searchengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Lemma implements Comparable<Lemma>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String lemma;

    @Column(columnDefinition = "INT", nullable = false)
    private int frequency;

    @Override
    public int compareTo(Lemma o) {
        return this.frequency - o.getFrequency();
    }

    @Override
    public String toString() {
        return "Lemma{" +
                "lemma='" + lemma + '\'' +
                '}';
    }
}

package searchengine.model.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@lombok.Data
@NoArgsConstructor
public class Data implements Comparable<Data>{
    String site;
    String siteName;
    String uri;
    String title;
    String snippet;
    double relevance;

    public Data(String site, String siteName, String uri, String title, String snippet, double relevance) {
        this.site = site;
        this.siteName = siteName;
        this.uri = uri;
        this.title = title;
        this.snippet = snippet;
        this.relevance = relevance;
    }

    @Override
    public int compareTo(Data o) {
        return (int) (o.getRelevance() * 10 - this.relevance * 10);
    }
}
package searchengine.dto.statistics.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.model.search.Data;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class SearchResponse {
    private boolean result;
    private long count;
    private ArrayList<Data> data;

    @Autowired
    public SearchResponse(boolean result, long count, ArrayList<Data> data) {
        this.result = result;
        this.count = count;
        this.data = data;
    }
}
package searchengine.dto.statistics;

import lombok.Data;
import org.springframework.stereotype.Service;


@Data
@Service
public class DetailedStatisticsItem {

    private String url;
    private String name;
    private String status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;

}

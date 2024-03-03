package searchengine.dto.statistics.responses;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
public class ErrorResponse {
    private boolean result;
    private String error;


    public ErrorResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
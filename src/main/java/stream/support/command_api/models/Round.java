package stream.support.command_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id", "name", "challenges"})
public class Round {
    private String status;
    private List<Match> matches;

    public boolean isCompleted(){
        return status.equalsIgnoreCase("COMPLETED");
    }
}

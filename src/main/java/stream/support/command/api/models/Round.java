package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id", "name", "challenges"})
public class Round {
    private String status;
    private List<Match> matches;

    public boolean isCompleted() {
        return status.equalsIgnoreCase("COMPLETED");
    }
}

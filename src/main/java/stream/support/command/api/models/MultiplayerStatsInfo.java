package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"typeid", "accountid", "progression", "division", "division_next"})
public class MultiplayerStatsInfo {
    String typename;
    Integer rank;
    Integer score;
}
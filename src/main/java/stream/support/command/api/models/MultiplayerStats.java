package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("total")
public class MultiplayerStats {
    MultiplayerStatsInfo info;
}
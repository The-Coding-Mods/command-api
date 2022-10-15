package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"timestamp", "clubtag", "clubtagtimestamp", "trophies", "meta"})
public class MMPlayer {
    String accountid;
    String displayname;
    String clubtag;

    Zone zone;
    List<MultiplayerStats> matchmaking;
}

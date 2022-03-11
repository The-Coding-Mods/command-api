package stream.support.command_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    private long id;
    private String name;
    private int players;
    @JsonProperty("starttime")
    private long startTime;
    @JsonProperty("endtime")
    private long endTime;

}

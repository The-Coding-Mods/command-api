package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Competition {
    private long id;
    private String name;
    private int players;
    @JsonProperty("starttime")
    private long startTime;
    @JsonProperty("endtime")
    private long endTime;

}

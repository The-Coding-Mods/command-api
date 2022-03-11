package stream.support.command_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"meta"})
public class Player {

    @JsonProperty("name")
    private String displayName;
    private String tag;
    private String id;
    private Zone zone;
}

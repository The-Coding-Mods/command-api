package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@JsonIgnoreProperties({"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    private String name;
    private String flag;
    private Zone parent;
}

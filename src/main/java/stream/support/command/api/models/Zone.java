package stream.support.command.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

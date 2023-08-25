package stream.support.command.api.models.kacky;

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
@JsonIgnoreProperties({"_id", "endAt", "name", "nbMaps", "startAt", "startMap", "current", "game"})
public class KackySeason {
    private List<KackyMap> maps;
}

package stream.support.command.api.models.kacky;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"_id", "seasonId", "time", "finishedAt", "trolled", "first", "favorite", "image", "validated",
    "difficulty", "coms", "fails"})
public class KackyMap {
    private Integer number;
    private String video;
}

package stream.support.command_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id", "numplayers", "liveid", "creatorplayer", "name", "description", "registrationstart", "registrationend", "startdate", "enddate", "leaderboardid", "manialink", "rulesurl", "streamurl", "websiteurl", "logourl", "verticalurl"})
public class Cotd {
    private List<Round> rounds;

    public boolean isInvalid(){
        return rounds == null || rounds.isEmpty() || !rounds.get(0).isCompleted();
    }
}

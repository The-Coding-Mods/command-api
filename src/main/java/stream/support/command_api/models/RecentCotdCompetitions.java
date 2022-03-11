package stream.support.command_api.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecentCotdCompetitions {
    private List<Competition> competitions;
}

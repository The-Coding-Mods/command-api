package stream.support.command.api.repositories;

import org.springframework.stereotype.Repository;
import stream.support.command.api.models.MMPlayer;
import stream.support.command.api.network.HTTPRequests;

import java.util.Optional;

@Repository
public class MatchMakingRepository {

    private final HTTPRequests httpRequests;

    public MatchMakingRepository(HTTPRequests httpRequests) {
        this.httpRequests = httpRequests;
    }


    public Optional<Integer> getPlayerPosition(String playerId) {
        Optional<MMPlayer> mmPlayer = httpRequests.getPlayer(playerId);
        Optional<Integer> position = Optional.empty();
        if (mmPlayer.isPresent()) {
            position = Optional.of(mmPlayer.get().getMatchmaking().get(0).getInfo().getRank());
        }
        return position;
    }

    public Optional<Integer> getPlayerPoints(String playerId) {
        Optional<MMPlayer> mmPlayer = httpRequests.getPlayer(playerId);
        Optional<Integer> position = Optional.empty();
        if (mmPlayer.isPresent()) {
            position = Optional.of(mmPlayer.get().getMatchmaking().get(0).getInfo().getScore());
        }
        return position;
    }
}
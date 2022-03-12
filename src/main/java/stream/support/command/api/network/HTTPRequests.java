package stream.support.command.api.network;

import org.springframework.stereotype.Service;
import stream.support.command.api.models.RecentCotdCompetitions;
import stream.support.command.api.models.Cotd;
import stream.support.command.api.models.CotdResult;
import stream.support.command.api.models.PlayerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HTTPRequests {

    public Cotd getCotdByCompId(long compId) {
        GetRequest<Cotd> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/comp/" + compId, Cotd.class);
        Optional<Cotd> optional = cotdHistoryGetRequest.execute();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public List<PlayerResult> getCotdResultsForMatch(long compId, long matchId) {
        int i = 0;
        List<PlayerResult> results = new ArrayList<>();
        List<PlayerResult> latestPlayerResults = new ArrayList<>();
        do {
            GetRequest<CotdResult> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/comp/" + compId + "/match/" + matchId + "/" + i, CotdResult.class);
            Optional<CotdResult> response = cotdHistoryGetRequest.execute();
            if (response.isPresent()) {
                latestPlayerResults = response.get().getPlayerResult();
                results.addAll(response.get().getPlayerResult());
            }
            i++;
        } while (!latestPlayerResults.isEmpty());

        return results;
    }

    public RecentCotdCompetitions getRecentCotdCompetitions() {
        GetRequest<RecentCotdCompetitions> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/cotd/0", RecentCotdCompetitions.class);
        Optional<RecentCotdCompetitions> optional = cotdHistoryGetRequest.execute();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}

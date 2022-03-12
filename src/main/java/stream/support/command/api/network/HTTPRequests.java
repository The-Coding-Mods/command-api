package stream.support.command.api.network;

import org.springframework.stereotype.Service;
import stream.support.command.api.models.Cotd;
import stream.support.command.api.models.CotdResult;
import stream.support.command.api.models.PlayerResult;
import stream.support.command.api.models.RecentCotdCompetitions;

import java.util.*;

@Service
public class HTTPRequests {

    private static final Map<String, String> TM_IO_API_HEADERS = new HashMap<>();

    static {
        TM_IO_API_HEADERS.put("User-Agent", "latest-cotd-player-position by Tag365 (Tag365#0365)");
    }

    public Cotd getCotdByCompId(long compId) {
        GetRequest<Cotd> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/comp/" + compId, Cotd.class, TM_IO_API_HEADERS);
        Optional<Cotd> optional = cotdHistoryGetRequest.execute().parse().getParsedResponse();
        return optional.orElse(null);
    }

    public List<PlayerResult> getCotdResultsForMatch(long compId, long matchId) {
        int i = 0;
        List<PlayerResult> results = new ArrayList<>();
        List<PlayerResult> latestPlayerResults = new ArrayList<>();
        do {
            GetRequest<CotdResult> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/comp/" + compId + "/match/" + matchId + "/" + i, CotdResult.class, TM_IO_API_HEADERS);
            Optional<CotdResult> response = cotdHistoryGetRequest.execute().parse().getParsedResponse();
            if (response.isPresent()) {
                latestPlayerResults = response.get().getPlayerResult();
                results.addAll(response.get().getPlayerResult());
            }
            i++;
        } while (!latestPlayerResults.isEmpty());

        return results;
    }

    public String getFollowage(String channel, String user) {
        GetRequest<String> followage = new GetRequest<>("https://api.crunchprank.net/twitch/followage/" + channel + "/" + user + "?precision=4", String.class, new HashMap<>());
        return followage.execute().getRawResponse();
    }

    public RecentCotdCompetitions getRecentCotdCompetitions() {
        GetRequest<RecentCotdCompetitions> cotdHistoryGetRequest = new GetRequest<>("https://trackmania.io/api/cotd/0", RecentCotdCompetitions.class, TM_IO_API_HEADERS);
        Optional<RecentCotdCompetitions> optional = cotdHistoryGetRequest.execute().parse().getParsedResponse();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
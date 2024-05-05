package stream.support.command.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import stream.support.command.api.models.Cotd;
import stream.support.command.api.models.CotdResult;
import stream.support.command.api.models.MMPlayer;
import stream.support.command.api.models.PlayerResult;
import stream.support.command.api.models.RecentCotdCompetitions;
import stream.support.command.api.models.kacky.WingoKackyElement;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Service
public class HTTPRequests {

    private final String followageToken;

    private static final Map<String, String> TM_IO_API_HEADERS = new HashMap<>();

    static {
        TM_IO_API_HEADERS.put("User-Agent", "latest-cotd-player-position by Tag365 (Tag365#0365)");
    }

    public HTTPRequests(@Value("${followage.token}") String followageToken) {
        this.followageToken = followageToken;
    }

    /**
     * Request COTD matches.
     *
     * @param compId ID of the COTD
     * @return COTD DAO
     */
    public Cotd getCotdByCompId(long compId) {
        GetRequest<Cotd> cotdHistoryGetRequest =
            new GetRequest<>("https://trackmania.io/api/comp/" + compId, Cotd.class, TM_IO_API_HEADERS);
        Optional<Cotd> optional = cotdHistoryGetRequest.execute().parse().getParsedResponse();
        return optional.orElse(null);
    }

    /**
     * Request player results of the cotd.
     * They are provided in multiple pages so do request until empty response
     *
     * @param compId  ID of the COTD
     * @param matchId ID of the match
     * @return List of all Players that competed in that match
     */
    public List<PlayerResult> getCotdResultsForMatch(long compId, long matchId) {
        int i = 0;
        List<PlayerResult> results = new ArrayList<>();
        List<PlayerResult> latestPlayerResults = new ArrayList<>();
        do {
            GetRequest<CotdResult> cotdHistoryGetRequest =
                new GetRequest<>("https://trackmania.io/api/comp/" + compId + "/match/" + matchId + "/" + i,
                    CotdResult.class, TM_IO_API_HEADERS);
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
        GetRequest<String> followage =
            new GetRequest<>(
                "https://decapi.me/twitch/followage/" + channel + "/" + user + "?precision=4&token=" + followageToken,
                String.class, new HashMap<>());
        return followage.execute().getRawResponse();
    }

    public RecentCotdCompetitions getRecentCotdCompetitions() {
        GetRequest<RecentCotdCompetitions> cotdHistoryGetRequest =
            new GetRequest<>("https://trackmania.io/api/cotd/0", RecentCotdCompetitions.class, TM_IO_API_HEADERS);
        Optional<RecentCotdCompetitions> optional = cotdHistoryGetRequest.execute().parse().getParsedResponse();
        return optional.orElse(null);
    }

    public Optional<MMPlayer> getPlayer(String playerId) {
        GetRequest<MMPlayer> mmPlayer =
            new GetRequest<>("https://trackmania.io/api/player/" + playerId, MMPlayer.class, TM_IO_API_HEADERS);
        return mmPlayer.execute().parse().getParsedResponse();
    }

    public Optional<WingoKackyElement> getKackySeason() {
        GetRequest<WingoKackyElement> kackySeason =
            new GetRequest<>("https://kacky.xat0mz.dev/seasons/eb1d6a58-cdd4-4ee4-98ac-e90da6d3bdda?format=json",
                WingoKackyElement.class,
                new HashMap<>());
        return kackySeason.execute().parse().getParsedResponse();
    }
}

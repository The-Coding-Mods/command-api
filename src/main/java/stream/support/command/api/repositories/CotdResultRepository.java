package stream.support.command.api.repositories;

import jakarta.annotation.PostConstruct;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stream.support.command.api.models.Competition;
import stream.support.command.api.models.Cotd;
import stream.support.command.api.models.PlayerResult;
import stream.support.command.api.models.RecentCotdCompetitions;
import stream.support.command.api.network.HTTPRequests;
import stream.support.command.api.util.Cache;

@Slf4j
@Service
public class CotdResultRepository {
    private final HTTPRequests httpRequests;
    private final Cache cache;
    private Clock clock;

    @Autowired
    public CotdResultRepository(HTTPRequests requests, Cache cache) {
        this.httpRequests = requests;
        this.cache = cache;
        this.clock = Clock.system(ZoneId.of("Europe/Paris"));
    }

    /**
     * Check if new result needs to be retrieved or cache can be used.
     * Get the position from the corresponding source and return it.
     *
     * @param playerId TM User of the player
     * @return Optional of the last position, empty if not played
     */
    public Optional<Integer> getLastPlayerPosition(String playerId) {
        LocalDateTime timeNow = LocalDateTime.now(clock);
        final LocalDateTime cotdTime = getTodayCotdTime(timeNow);
        log.info("CotdTime: {}", cotdTime);
        log.info("CachedTime: {}", cache.getLastUpdated());

        LocalDateTime fromCache = cache.getLastUpdated();
        Optional<Integer> relevantPosition = Optional.empty();
        // Only try to update after 19
        // check if already updated after 19 (the latest change is after cotd start time)
        if ((timeNow.getHour() >= 19 && fromCache.isBefore(cotdTime)) || fromCache.isBefore(cotdTime.minusDays(1))) {
            log.info("Get new cotd results");
            log.info(timeNow.toString());
            getLastCotdResults(timeNow, timeNow.getHour() < 19);
            final Optional<PlayerResult> optional = findPlayerByPlayerId(cache.getLastCotdDiv1Results(), playerId);
            if (optional.isPresent()) {
                relevantPosition = Optional.of(optional.get().getPosition());
            }
        } else {
            final Optional<PlayerResult> positionOptional =
                findPlayerByPlayerId(cache.getLastCotdDiv1Results(), playerId);
            if (positionOptional.isPresent()) {
                log.info("Use cached cotd results");
                PlayerResult position = positionOptional.get();
                relevantPosition = Optional.of(position.getPosition());
            }
        }
        return relevantPosition;
    }


    public String getLatestCotdWinner() {
        LocalDateTime timeNow = LocalDateTime.now(clock);
        final LocalDateTime cotdTime = getTodayCotdTime(timeNow);
        LocalDateTime fromCache = cache.getLastUpdated();
        if ((timeNow.getHour() >= 19 && fromCache.isBefore(cotdTime)) || fromCache.isBefore(cotdTime.minusDays(1))) {
            log.info("Get new cotd results");
            log.info(timeNow.toString());
            getLastCotdResults(timeNow, timeNow.getHour() < 19);
        } else {
            log.info("Use cached cotd results");
        }
        return cache.getLastCotdDiv1Results().get(0).getPlayer().getDisplayName();
    }

    /**
     * Loop through the {@code resultList} and filter by {@code playerId}.
     *
     * @param resultList List of all results either from cache or new
     * @param playerId   TM User of the player
     * @return Optional if player was found in the list
     */
    private Optional<PlayerResult> findPlayerByPlayerId(List<PlayerResult> resultList, String playerId) {
        return resultList.stream().filter(player -> player.getPlayer().getId().trim().equalsIgnoreCase(playerId.trim()))
            .findFirst();
    }

    /**
     * Send request to tm.io api:
     * - get competition id
     * - get match id from competition
     * - get results (multiple pages)
     *
     * @param time          Current time
     * @param yesterdayCotd true if the cotd from yesterday needs to be looked at (before 19 o'clock)
     */
    private void getLastCotdResults(LocalDateTime time, boolean yesterdayCotd) {
        RecentCotdCompetitions cotdRecentHistory = httpRequests.getRecentCotdCompetitions();
        if (yesterdayCotd) {
            time = time.minusDays(1);
        }
        String dayString = String.format("%d-%02d-%02d", time.getYear(), time.getMonthValue(), time.getDayOfMonth());

        Optional<Competition> optional = cotdRecentHistory.getCompetitions().stream()
            .filter(c -> c.getName().contains("#1") && c.getName().contains(dayString)).findFirst();
        if (optional.isPresent()) {
            Cotd cotd = httpRequests.getCotdByCompId(optional.get().getId());
            if (!cotd.isInvalid()) {
                List<PlayerResult> cotdResult = httpRequests.getCotdResultsForMatch(optional.get().getId(),
                    cotd.getRounds().get(0).getMatches().get(0).getId());
                if (!cotdResult.isEmpty()) {
                    cotdResult = cotdResult.stream().filter(playerResult -> playerResult.getPosition() != 0)
                        .sorted(Comparator.comparingInt(PlayerResult::getPosition)).toList();
                    cache.setLastCotdDiv1Results(cotdResult);
                    cache.setLastUpdated(LocalDateTime.now(ZoneId.of("Europe/Paris")));
                }
            }
        }
    }

    private LocalDateTime getTodayCotdTime(LocalDateTime now) {
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 19, 30);
    }

    /**
     * Cache last cotd on startup.
     */
    @PostConstruct
    private void postConstruct() {
        log.info("Get cotd results after startup");
        LocalDateTime timeNow = LocalDateTime.now();
        getLastCotdResults(timeNow, timeNow.getHour() < 19);
    }

    protected void setClock(Clock clock) {
        this.clock = clock;
    }
}

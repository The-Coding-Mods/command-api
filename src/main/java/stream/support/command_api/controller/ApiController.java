package stream.support.command_api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command_api.models.Competition;
import stream.support.command_api.models.Cotd;
import stream.support.command_api.models.PlayerResult;
import stream.support.command_api.models.RecentCotdCompetitions;
import stream.support.command_api.network.HTTPRequests;
import stream.support.command_api.util.Cache;
import stream.support.command_api.util.StringUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ApiController {

    private final Cache cache;
    private final HTTPRequests httpRequests;

    public ApiController(Cache cache, HTTPRequests httpRequests) {
        this.cache = cache;
        this.httpRequests = httpRequests;
    }

    @GetMapping("/cotd/position")
    public ResponseEntity<String> getLatestCotdPosition(@RequestParam String playerId) {
        LocalDateTime timeNow = LocalDateTime.now();
        final LocalDateTime cotdTime = getTodayCotdTime(timeNow);
        log.info("CotdTime: {}", cotdTime);
        log.info("CachedTime: {}", cache.getLastUpdated());

        LocalDateTime fromCache = cache.getLastUpdated();
        int relevantPosition = -1;
        if ((timeNow.getHour() >= 19 && fromCache.isBefore(cotdTime)) || fromCache.isBefore(cotdTime.minusDays(1))) {
            log.info("Get new cotd results");
            log.info(timeNow.toString());
            final Optional<PlayerResult> optional = findPlayerByPlayerId(getLastCotdResults(timeNow, timeNow.getHour() < 19), playerId);
            if (optional.isPresent()) {
                relevantPosition = optional.get().getPosition();
            }
        } else {
            final Optional<PlayerResult> positionOptional = findPlayerByPlayerId(cache.getLastCotdDiv1Results(), playerId);
            if (positionOptional.isPresent()) {
                log.info("Use cached cotd results");
                PlayerResult position = positionOptional.get();
                relevantPosition = position.getPosition();
            }
        }

        if (relevantPosition == -1) {
            return ResponseEntity.ok("did not participate");
        }
        return ResponseEntity.ok(StringUtil.ordinal(relevantPosition));
    }

    private Optional<PlayerResult> findPlayerByPlayerId(List<PlayerResult> result, String playerId) {
        return result.stream().filter(player -> player.getPlayer().getId().trim().equalsIgnoreCase(playerId.trim())).findFirst();
    }

    private List<PlayerResult> getLastCotdResults(LocalDateTime time, boolean yesterdayCotd) {
        RecentCotdCompetitions cotdRecentHistory = httpRequests.getRecentCotdCompetitions();
        if (yesterdayCotd) time = time.minusDays(1);
        String dayString = String.format("%d-%02d-%02d", time.getYear(), time.getMonthValue(), time.getDayOfMonth());

        Optional<Competition> optional = cotdRecentHistory.getCompetitions().stream().filter(c -> c.getName().contains("#1") && c.getName().contains(dayString)).findFirst();
        if (optional.isPresent()) {
            Cotd cotd = httpRequests.getCotdByCompId(optional.get().getId());
            if (!cotd.isInvalid()) {
                List<PlayerResult> cotdResult = httpRequests.getCotdResultsForMatch(optional.get().getId(), cotd.getRounds().get(0).getMatches().get(0).getId());
                if (!cotdResult.isEmpty()) {
                    cache.setLastCotdDiv1Results(cotdResult);
                    cache.setLastUpdated(LocalDateTime.now());
                    return cotdResult;
                }
            }
        }
        return new ArrayList<>();
    }

    private LocalDateTime getTodayCotdTime(LocalDateTime now) {
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 18, 45);
    }

    @PostConstruct
    private void postConstruct() {
        log.info("Get cotd results after startup");
        LocalDateTime timeNow = LocalDateTime.now();
        getLastCotdResults(timeNow, timeNow.getHour() < 19);
    }
}

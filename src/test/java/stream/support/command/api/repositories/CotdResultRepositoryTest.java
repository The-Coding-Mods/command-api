package stream.support.command.api.repositories;

import org.junit.jupiter.api.Test;
import stream.support.command.api.models.*;
import stream.support.command.api.network.HTTPRequests;
import stream.support.command.api.util.Cache;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CotdResultRepositoryTest {
    private final HTTPRequests httpRequests = mock(HTTPRequests.class);
    private final Cache cache = mock(Cache.class);
    private final CotdResultRepository repository = new CotdResultRepository(httpRequests, cache);

    @Test
    void shouldReturnPositionAndUseCachedResults() {
        repository.setClock(Clock.fixed(Instant.parse("2022-06-16T16:30:00.0Z"), ZoneId.of("Europe/Paris")));
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-06-16T14:00:00.0Z"), ZoneId.of("Europe/Paris"))));
        when(cache.getLastCotdDiv1Results()).thenReturn(createPlayerList());

        Optional<Integer> response = repository.getLastPlayerPosition("1");

        assertTrue(response.isPresent());
        assertEquals(1, response.get());
        verify(cache, times(1)).getLastCotdDiv1Results();
    }

    @Test
    void shouldReturnPositionAndUseCachedResultsAfter19Hours() {
        repository.setClock(Clock.fixed(Instant.parse("2022-06-16T18:30:00.0Z"), ZoneId.of("Europe/Paris")));
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-06-16T17:50:00.0Z"), ZoneId.of("Europe/Paris"))));
        when(cache.getLastCotdDiv1Results()).thenReturn(createPlayerList());

        Optional<Integer> response = repository.getLastPlayerPosition("1");

        assertTrue(response.isPresent());
        assertEquals(1, response.get());
        verify(cache, times(1)).getLastCotdDiv1Results();
    }

    @Test
    void shouldReturnEmptyOptionalAndUseCachedResults() {
        repository.setClock(Clock.fixed(Instant.parse("2022-06-16T16:30:00.0Z"), ZoneId.of("Europe/Paris")));
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-06-16T14:00:00.0Z"), ZoneId.of("Europe/Paris"))));
        when(cache.getLastCotdDiv1Results()).thenReturn(createPlayerList());

        Optional<Integer> response = repository.getLastPlayerPosition("7");

        assertTrue(response.isEmpty());
        verify(cache, times(1)).getLastCotdDiv1Results();
    }

    @Test
    void shouldRetrieveNewCotdResults() {
        Clock clock = Clock.fixed(Instant.parse("2022-06-16T17:30:00.0Z"), ZoneId.of("Europe/Paris"));
        repository.setClock(clock);
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-06-16T14:00:00.0Z"), ZoneId.of("Europe/Paris"))));
        LocalDateTime time = LocalDateTime.now(clock);
        String dayString = String.format("%d-%02d-%02d", time.getYear(), time.getMonthValue(), time.getDayOfMonth());

        when(httpRequests.getRecentCotdCompetitions()).thenReturn(createRecentCotdCompetitions(dayString));
        when(httpRequests.getCotdByCompId(1)).thenReturn(createCotd());
        when(httpRequests.getCotdResultsForMatch(1,1)).thenReturn(createPlayerList());

        repository.getLastPlayerPosition("1");

        verify(httpRequests, times(1)).getRecentCotdCompetitions();
        verify(httpRequests, times(1)).getCotdByCompId(anyLong());
        verify(httpRequests, times(1)).getCotdResultsForMatch(anyLong(), anyLong());
    }

    @Test
    void shouldTryToRetrieveNewCotdResultsWhenMatchNotFinished() {
        Clock clock = Clock.fixed(Instant.parse("2022-06-16T17:30:00.0Z"), ZoneId.of("Europe/Paris"));
        repository.setClock(clock);
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-06-16T14:00:00.0Z"), ZoneId.of("Europe/Paris"))));
        LocalDateTime time = LocalDateTime.now(clock);
        String dayString = String.format("%d-%02d-%02d", time.getYear(), time.getMonthValue(), time.getDayOfMonth());

        when(httpRequests.getRecentCotdCompetitions()).thenReturn(createRecentCotdCompetitions(dayString));
        when(httpRequests.getCotdByCompId(1)).thenReturn(createCotd("incomplete"));
        when(httpRequests.getCotdResultsForMatch(1,1)).thenReturn(createPlayerList());

        repository.getLastPlayerPosition("1");

        verify(httpRequests, times(1)).getRecentCotdCompetitions();
        verify(httpRequests, times(1)).getCotdByCompId(anyLong());
        verify(httpRequests, times(0)).getCotdResultsForMatch(anyLong(), anyLong());
    }

    @Test
    void shouldRetrieveNewCotdWhenCacheIsBeforePreviousCotd() {
        Clock clock = Clock.fixed(Instant.parse("2022-06-16T13:30:00.0Z"), ZoneId.of("Europe/Paris"));
        repository.setClock(clock);
        when(cache.getLastUpdated()).thenReturn(LocalDateTime.now(Clock.fixed(Instant.parse("2022-05-16T14:00:00.0Z"), ZoneId.of("Europe/Paris"))));
        LocalDateTime time = LocalDateTime.now(clock).minusDays(1);
        String dayString = String.format("%d-%02d-%02d", time.getYear(), time.getMonthValue(), time.getDayOfMonth());

        when(httpRequests.getRecentCotdCompetitions()).thenReturn(createRecentCotdCompetitions(dayString));
        when(httpRequests.getCotdByCompId(1)).thenReturn(createCotd());
        when(httpRequests.getCotdResultsForMatch(1,1)).thenReturn(createPlayerList());

        repository.getLastPlayerPosition("1");

        verify(httpRequests, times(1)).getRecentCotdCompetitions();
        verify(httpRequests, times(1)).getCotdByCompId(anyLong());
        verify(httpRequests, times(1)).getCotdResultsForMatch(anyLong(), anyLong());
    }

    private Cotd createCotd() {
        return new Cotd(createRounds("COMPLETED"));
    }

    private Cotd createCotd(String status) {
        return new Cotd(createRounds(status));
    }

    private List<Round> createRounds(String status) {
        return List.of(new Round(status, createMatches(status.equalsIgnoreCase("completed"))));
    }
    private List<Round> createRounds(String roundStatus, boolean matchStatus) {
        return List.of(new Round(roundStatus, createMatches(matchStatus)));
    }

    private List<Match> createMatches(boolean status) {
        return List.of(new Match(1, "1", status));
    }

    private RecentCotdCompetitions createRecentCotdCompetitions(String dayString) {
        return new RecentCotdCompetitions(createCompetitions(dayString));
    }

    private List<Competition> createCompetitions(String dayString) {
        List<Competition> competitions = new ArrayList<>();
        competitions.add(new Competition(1, "Cup of the Day " + dayString + " #1", 5, 0,100));
        competitions.add(new Competition(2, "Cup of the Day " + dayString + " #2", 5, 0,100));
        competitions.add(new Competition(3, "Cup of the Day " + dayString + " #3", 5, 0,100));
        return competitions;
    }

    private List<PlayerResult> createPlayerList() {
        List<PlayerResult> results = new ArrayList<>();
        results.add(new PlayerResult(createPlayerWithId("1"), 1));
        results.add(new PlayerResult(createPlayerWithId("2"), 2));
        results.add(new PlayerResult(createPlayerWithId("3"), 3));
        results.add(new PlayerResult(createPlayerWithId("4"), 4));
        results.add(new PlayerResult(createPlayerWithId("5"), 5));
        return results;
    }

    private Player createPlayerWithId(String id) {
        return new Player(id, "", id, new Zone());
    }
}
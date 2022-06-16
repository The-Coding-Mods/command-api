package stream.support.command.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import stream.support.command.api.repositories.CotdResultRepository;
import stream.support.command.api.repositories.FollowageRepository;

import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiControllerTest {

    private final FollowageRepository followageRepository = mock(FollowageRepository.class);
    private final CotdResultRepository cotdRepository = mock(CotdResultRepository.class);

    private final ApiController apiController = new ApiController(cotdRepository, followageRepository);

    @Test
    void shouldReturnPlayerPositionFirst() {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(1));
        ResponseEntity<String> response = apiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1st", response.getBody());
    }
    @Test
    void shouldReturnPlayerPositionSecond() {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(2));
        ResponseEntity<String> response = apiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2nd", response.getBody());
    }
    @Test
    void shouldReturnPlayerPositionThird() {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(3));
        ResponseEntity<String> response = apiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("3rd", response.getBody());
    }

    @Test
    void shouldReturnPlayerPositionFourthToTenth() {
        for (int i = 4; i < 11; i ++) {
            when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(i));
            ResponseEntity<String> response = apiController.getLatestCotdPosition("1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(i+"th", response.getBody());
        }
    }

    @Test
    void shouldReturnNotParticipate() {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(null));
        ResponseEntity<String> response = apiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("did not participate", response.getBody());
    }

    private <T> Optional<T> createOptional(T i) {
        return Optional.ofNullable(i);
    }
}
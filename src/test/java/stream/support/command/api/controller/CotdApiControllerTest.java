package stream.support.command.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import stream.support.command.api.repositories.CotdResultRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CotdApiControllerTest {

    private final CotdResultRepository cotdRepository = mock(CotdResultRepository.class);

    private final CotdApiController cotdApiController = new CotdApiController(cotdRepository);

    @ParameterizedTest
    @CsvSource({
            "1, 1st",
            "2, 2nd",
            "3, 3rd",
            "4, 4th",
            "5, 5th",
            "6, 6th",
            "7, 7th",
            "8, 8th",
            "9, 9th",
            "10, 10th",
    })
    void shouldReturnCorrectPlayerPosition(Integer position, String expected) {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(position));
        ResponseEntity<String> response = cotdApiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expected, response.getBody());
    }

    @Test
    void shouldReturnNotParticipate() {
        when(cotdRepository.getLastPlayerPosition("1")).thenReturn(createOptional(null));
        ResponseEntity<String> response = cotdApiController.getLatestCotdPosition("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("did not participate", response.getBody());
    }

    private <T> Optional<T> createOptional(T i) {
        return Optional.ofNullable(i);
    }
}

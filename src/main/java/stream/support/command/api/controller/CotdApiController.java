package stream.support.command.api.controller;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.CotdResultRepository;
import stream.support.command.api.util.StringUtil;

@Slf4j
@RestController
public class CotdApiController {

    private final CotdResultRepository cotdResultRepository;

    public CotdApiController(CotdResultRepository repository) {
        this.cotdResultRepository = repository;
    }


    @GetMapping("/cotd/position")
    public ResponseEntity<String> getLatestCotdPosition(@RequestParam String playerId) {

        Optional<Integer> relevantPosition = cotdResultRepository.getLastPlayerPosition(playerId);

        return relevantPosition.map(integer -> ResponseEntity.ok(StringUtil.ordinal(integer)))
            .orElseGet(() -> ResponseEntity.ok("did not participate"));
    }

    @GetMapping("/cotd/winner")
    public ResponseEntity<String> getLatestCotdWinner() {
        return ResponseEntity.ok(cotdResultRepository.getLatestCotdWinner());
    }

}

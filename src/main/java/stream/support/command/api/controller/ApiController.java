package stream.support.command.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.CotdResultRepository;
import stream.support.command.api.util.StringUtil;

import java.util.Optional;

@RestController
@Slf4j
public class ApiController {

    private final CotdResultRepository repository;

    public ApiController(CotdResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cotd/position")
    public ResponseEntity<String> getLatestCotdPosition(@RequestParam String playerId) {

        Optional<Integer> relevantPosition = repository.getLastPlayerPosition(playerId);

        return relevantPosition.map(integer -> ResponseEntity.ok(StringUtil.ordinal(integer))).orElseGet(() -> ResponseEntity.ok("did not participate"));
    }
}

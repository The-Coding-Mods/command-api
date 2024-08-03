package stream.support.command.api.controller;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.MatchMakingRepository;

@RestController
public class MatchMakingApiController {

    private final MatchMakingRepository matchMakingRepository;

    public MatchMakingApiController(MatchMakingRepository matchMakingRepository) {
        this.matchMakingRepository = matchMakingRepository;
    }

    @GetMapping("/match-making/{playerId}/rank")
    public ResponseEntity<Integer> getPlayerMatchMakingRank(@PathVariable String playerId) {
        Optional<Integer> mmPosition = matchMakingRepository.getPlayerPosition(playerId);

        return mmPosition.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/match-making/{playerId}/points")
    public ResponseEntity<Integer> getPlayerMatchMakingPoints(@PathVariable String playerId) {
        Optional<Integer> mmPosition = matchMakingRepository.getPlayerPoints(playerId);

        return mmPosition.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

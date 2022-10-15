package stream.support.command.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.MatchMakingRepository;

import java.util.Optional;

@RestController
public class MatchMakingApiController {

    private final MatchMakingRepository matchMakingRepository;

    public MatchMakingApiController(MatchMakingRepository matchMakingRepository) {
        this.matchMakingRepository = matchMakingRepository;
    }

    @GetMapping("/match-making/rank/{playerId}")
    public ResponseEntity<Integer> getPlayerMatchMakingRank(@PathVariable String playerId) {
        Optional<Integer> mmPosition = matchMakingRepository.getPlayerPosition(playerId);

        return mmPosition.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/match-making/points/{playerId}")
    public ResponseEntity<Integer> getPlayerMatchMakingPoints(@PathVariable String playerId) {
        Optional<Integer> mmPosition = matchMakingRepository.getPlayerPoints(playerId);

        return mmPosition.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
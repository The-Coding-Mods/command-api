package stream.support.command.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.CotdResultRepository;
import stream.support.command.api.repositories.FollowageRepository;
import stream.support.command.api.util.StringUtil;

import java.util.Optional;

@Slf4j
@RestController
public class ApiController {

    private final CotdResultRepository cotdResultRepository;
    private final FollowageRepository followageRepository;


    public ApiController(CotdResultRepository repository, FollowageRepository followageRepository) {
        this.cotdResultRepository = repository;
        this.followageRepository = followageRepository;
    }

    @GetMapping("/followage/{channel}/{user}")
    public ResponseEntity<String> getFollowAge(@PathVariable String channel, @PathVariable String user) {
        String followage = followageRepository.getFollowageForUser(channel, user);
        return ResponseEntity.ok(followage);
    }

    @GetMapping("/cotd/position")
    public ResponseEntity<String> getLatestCotdPosition(@RequestParam String playerId) {

        Optional<Integer> relevantPosition = cotdResultRepository.getLastPlayerPosition(playerId);

        return relevantPosition.map(integer -> ResponseEntity.ok(StringUtil.ordinal(integer))).orElseGet(() -> ResponseEntity.ok("did not participate"));
    }

}

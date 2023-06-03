package stream.support.command.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.repositories.FollowageRepository;

@RestController
public class FollowageApiController {

    private final FollowageRepository followageRepository;

    public FollowageApiController(FollowageRepository followageRepository) {
        this.followageRepository = followageRepository;
    }

    @GetMapping("/followage/{channel}/{user}")
    public ResponseEntity<String> getFollowAge(@PathVariable String channel, @PathVariable String user) {
        String followage = followageRepository.getFollowageForUser(channel, user);
        return ResponseEntity.ok(followage);
    }
}

package stream.support.command.api.controller;


import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import stream.support.command.api.models.kacky.KackyMap;
import stream.support.command.api.models.kacky.KackySeason;
import stream.support.command.api.models.kacky.WingoKackyElement;
import stream.support.command.api.network.HTTPRequests;

@RestController
public class KackyController {
    private final HTTPRequests httpRequests;

    public KackyController(HTTPRequests httpRequests) {
        this.httpRequests = httpRequests;
    }

    @GetMapping("/kacky/maps/{number}/clip")
    public ResponseEntity<String> getKackyClipForMap(@PathVariable Integer number) {
        Optional<WingoKackyElement> seasonOptional = httpRequests.getKackySeason();
        if (seasonOptional.isPresent()) {
            KackySeason season = seasonOptional.get().getSeason();
            List<KackyMap> maps = season.getMaps();
            Optional<KackyMap> map = maps.stream().filter(m -> m.getNumber().equals(number)).findFirst();
            if (map.isPresent() && map.get().getVideo() != null) {
                return ResponseEntity.ok(map.get().getVideo());
            }
            return ResponseEntity.ok("No clip found for map " + number);
        }
        return ResponseEntity.ok("Something went wrong with the API");
    }
}

package stream.support.command.api.util;

import org.springframework.stereotype.Service;
import stream.support.command.api.models.PlayerResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Cache {
    private LocalDateTime lastUpdated = LocalDateTime.MIN;
    private List<PlayerResult> lastCotdDiv1Results = new ArrayList<>();

    public void setLastCotdDiv1Results(List<PlayerResult> lastCotdDiv1Results) {
        this.lastCotdDiv1Results = lastCotdDiv1Results;
    }

    public List<PlayerResult> getLastCotdDiv1Results() {
        return lastCotdDiv1Results;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}

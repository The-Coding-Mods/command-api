package stream.support.command.api.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import stream.support.command.api.models.PlayerResult;

@Setter
@Getter
@Service
public class Cache {
    private LocalDateTime lastUpdated = LocalDateTime.MIN;
    private List<PlayerResult> lastCotdDiv1Results = new ArrayList<>();
}

package stream.support.command_api.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private long id;
    private String name;
    private boolean completed;
}

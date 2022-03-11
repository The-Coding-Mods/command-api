package stream.support.command_api.models;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    private String name;
    private String flag;

    private Zone parent;
}

package github.com.dm_zh.diploma_project.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode
public class MeetingView {
    private int id;

    private UserDto creator;

    private String topic;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean hasProtocol;

    private String video;

    private Set<UserDto> participants = new HashSet<>();
}

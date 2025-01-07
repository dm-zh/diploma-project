package github.com.dm_zh.diploma_project.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class NewMeetingDto {

    private String topic;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<String> participants = new HashSet<>();
}

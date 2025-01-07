package github.com.dm_zh.diploma_project.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MeetingsGrouped {
    List<MeetingView> old = new ArrayList<>();
    List<MeetingView> current = new ArrayList<>();
    List<MeetingView> future = new ArrayList<>();

}

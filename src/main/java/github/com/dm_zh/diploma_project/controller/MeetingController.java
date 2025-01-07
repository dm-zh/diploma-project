package github.com.dm_zh.diploma_project.controller;

import github.com.dm_zh.diploma_project.dto.MeetingView;
import github.com.dm_zh.diploma_project.dto.MeetingsGrouped;
import github.com.dm_zh.diploma_project.dto.NewMeetingDto;
import github.com.dm_zh.diploma_project.dto.ParticipantIdsDto;
import github.com.dm_zh.diploma_project.service.Service;
import github.com.dm_zh.diploma_project.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final Service service;

    @PostMapping
    public void create(@RequestBody NewMeetingDto newMeetingDto) {
        service.createMeeting(newMeetingDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.validateOwner(id);
        service.deleteMeeting(id);
    }

    @GetMapping("/my")
    public MeetingsGrouped my() {
        return service.getAllMeetingsByUserId(UserUtils.getCurrentUserId());
    }

    @PostMapping("/{id}/participants")
    public void addParticipants(@PathVariable int id, @RequestBody ParticipantIdsDto participantIdsDto) {
        service.validateOwner(id);
        service.addParticipantsToMeeting(id, participantIdsDto.getParticipantIds());
    }

    @DeleteMapping("/{id}/participants")
    public void deleteParticipants(@PathVariable int id, @RequestBody ParticipantIdsDto participantIdsDto) {
        service.validateOwner(id);
        service.deleteParticipantsFromMeeting(id, participantIdsDto.getParticipantIds());
    }
}

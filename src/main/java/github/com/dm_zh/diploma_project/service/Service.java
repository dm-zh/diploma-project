package github.com.dm_zh.diploma_project.service;

import github.com.dm_zh.diploma_project.dto.MeetingView;
import github.com.dm_zh.diploma_project.dto.MeetingsGrouped;
import github.com.dm_zh.diploma_project.dto.NewMeetingDto;
import github.com.dm_zh.diploma_project.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface Service {

    void updateUser(UserDto user);

    void createMeeting(NewMeetingDto newMeetingDto);

    void deleteMeeting(int meetingId);

    void addParticipantsToMeeting(int meetingId, Set<String> participantIds);

    void deleteParticipantsFromMeeting(int meetingId, Set<String> participantIds);

    List<UserDto> getAllUsers();

    MeetingsGrouped getAllMeetingsByUserId(String userId);

    void validateOwner(int meetingId);
}

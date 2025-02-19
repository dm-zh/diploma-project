package github.com.dm_zh.diploma_project.service;

import github.com.dm_zh.diploma_project.dto.MeetingsGrouped;
import github.com.dm_zh.diploma_project.dto.NewMeetingDto;
import github.com.dm_zh.diploma_project.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

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

    void validateOwnerOrParticipant(int meetingId);

    void addProtocolToMeeting(int id, String protocol);

    String getProtocol(int meetingId);

    void uploadVideo(int id, MultipartFile file);
}

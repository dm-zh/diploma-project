package github.com.dm_zh.diploma_project.service;

import github.com.dm_zh.diploma_project.dto.MeetingView;
import github.com.dm_zh.diploma_project.dto.MeetingsGrouped;
import github.com.dm_zh.diploma_project.dto.NewMeetingDto;
import github.com.dm_zh.diploma_project.dto.UserDto;
import github.com.dm_zh.diploma_project.entity.MeetingEntity;
import github.com.dm_zh.diploma_project.entity.UserEntity;
import github.com.dm_zh.diploma_project.repository.MeetingRepository;
import github.com.dm_zh.diploma_project.repository.UserRepository;
import github.com.dm_zh.diploma_project.utils.UserUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MinioClient minioClient;

    @Override
    @Transactional
    public void updateUser(UserDto user) {

        UserEntity userEntity = userRepository.findById(user.getId()).orElseGet(() -> new UserEntity(user.getId()));

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setLogin(user.getLogin());

        userRepository.save(userEntity);

    }

    @Override
    @Transactional
    public void createMeeting(NewMeetingDto newMeetingDto) {
        MeetingEntity meetingEntity = new MeetingEntity();
        meetingEntity.setTopic(newMeetingDto.getTopic());
        meetingEntity.setDescription(newMeetingDto.getDescription());
        meetingEntity.setStartTime(newMeetingDto.getStartTime());
        meetingEntity.setEndTime(newMeetingDto.getEndTime());

        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        meetingEntity.setCreator(userRepository
                .getReferenceById(Objects.requireNonNull(user.getAttribute("sub"))));

        for (String participantId : newMeetingDto.getParticipants()) {
            meetingEntity.getParticipants().add(userRepository
                    .getReferenceById(participantId));
        }

        meetingRepository.save(meetingEntity);
    }

    @Override
    public void deleteMeeting(int meetingId) {
        meetingRepository.deleteById(meetingId);
    }

    @Override
    @Transactional
    public void addParticipantsToMeeting(int meetingId, Set<String> participantIds) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

        for (String participantId : participantIds) {
            meetingEntity.getParticipants().add(userRepository
                    .getReferenceById(participantId));
        }

        meetingRepository.save(meetingEntity);
    }

    @Override
    public void deleteParticipantsFromMeeting(int meetingId, Set<String> participantIds) {

        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

        meetingEntity.getParticipants().removeIf(userEntity -> participantIds.contains(userEntity.getId()));

        meetingRepository.save(meetingEntity);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userEntity -> {
            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setFirstName(userEntity.getFirstName());
            userDto.setLastName(userEntity.getLastName());
            return userDto;
        }).toList();
    }

    @Override
    public MeetingsGrouped getAllMeetingsByUserId(String userId) {
        List<MeetingEntity> meetingsUserId = meetingRepository.findAllByUserId(userId);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        MeetingsGrouped answer = new MeetingsGrouped();
        for (MeetingEntity meetingEntity : meetingsUserId) {
            MeetingView meetingDto = new MeetingView();
            meetingDto.setId(meetingEntity.getId());
            meetingDto.setCreator(UserUtils.mapUserEntityToDto(meetingEntity.getCreator()));

            meetingDto.setStartTime(meetingEntity.getStartTime());
            meetingDto.setEndTime(meetingEntity.getEndTime());
            meetingDto.setTopic(meetingEntity.getTopic());
            meetingDto.setDescription(meetingEntity.getDescription());
            if(meetingEntity.getProtocol() != null){
                meetingDto.setHasProtocol(true);
            }
            meetingDto.setVideo(meetingEntity.getVideo());
            for (UserEntity participant : meetingEntity.getParticipants()) {
                meetingDto.getParticipants().add(UserUtils.mapUserEntityToDto(participant));
            }

            if (meetingDto.getEndTime().isBefore(now)) {
                answer.getOld().add(meetingDto);
            } else if (meetingDto.getStartTime().isAfter(now)) {
                answer.getFuture().add(meetingDto);
            } else {
                answer.getCurrent().add(meetingDto);
            }

        }

        return answer;
    }

    @Override
    public void validateOwner(int meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

        String userId = UserUtils.getCurrentUserId();
        if (!Objects.equals(meetingEntity.getCreator().getId(), userId)) {
            throw new SecurityException();
        }
    }


    @Override
    @Transactional
    public void validateOwnerOrParticipant(int meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

        String userId = UserUtils.getCurrentUserId();
        if (Objects.equals(meetingEntity.getCreator().getId(), userId)) {
            return;
        }

        for (UserEntity participant : meetingEntity.getParticipants()) {
            if (Objects.equals(participant.getId(), userId)) {
                return;
            }
        }


        throw new SecurityException();
    }

    @Override
    public void addProtocolToMeeting(int id, String protocol) {
        MeetingEntity meetingEntity = meetingRepository.findById(id).orElseThrow();
        meetingEntity.setProtocol(protocol);
        meetingRepository.save(meetingEntity);
    }

    @Override
    public String getProtocol(int meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();
        return meetingEntity.getProtocol();
    }

    private final static String BUCKET = "video";

    @Override
    @SneakyThrows
    public void uploadVideo(int id, MultipartFile file) {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET).build());
        }

        minioClient.putObject(
                PutObjectArgs.builder().bucket(BUCKET).object(String.valueOf(id)).stream(
                                file.getInputStream(), file.getInputStream().available(), -1)
                        .contentType(file.getContentType())
                        .build());
        MeetingEntity meetingEntity = meetingRepository.findById(id).orElseThrow();
        meetingEntity.setVideo("/" + id);
        meetingRepository.save(meetingEntity);

    }
}

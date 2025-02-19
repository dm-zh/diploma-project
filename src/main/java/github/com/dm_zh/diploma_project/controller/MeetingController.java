package github.com.dm_zh.diploma_project.controller;

import github.com.dm_zh.diploma_project.dto.*;
import github.com.dm_zh.diploma_project.service.Service;
import github.com.dm_zh.diploma_project.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    @PostMapping("/{id}/protocol")
    public void addProtocol(@PathVariable int id, @RequestBody ProtocolDto protocolDto) {
        service.validateOwnerOrParticipant(id);
        service.addProtocolToMeeting(id, protocolDto.getProtocol());
    }

    @GetMapping("/{id}/protocol")
      public ResponseEntity<InputStreamResource>  getProtocol(@PathVariable int id) throws IOException {
        service.validateOwnerOrParticipant(id);

        String filename = "protocol.txt";
        String content = service.getProtocol(id);
        Path filepath = Paths.get(filename);
        Path exportedFilePath = Files.write(filepath, content.getBytes(),
                StandardOpenOption.CREATE);

        // Download file with InputStreamResource
        File exportedFile = exportedFilePath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

    @PostMapping("/{id}/video")
    public ResponseEntity<Object> uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        service.validateOwnerOrParticipant(id);
        service.uploadVideo(id, file);


        return null;
    }
}

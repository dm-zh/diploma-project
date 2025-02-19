package github.com.dm_zh.diploma_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meeting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String topic;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String protocol;

    @Column(columnDefinition = "TEXT")
    private String video;

    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    @NotNull
    private UserEntity creator;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "meetings_users",
            joinColumns = { @JoinColumn(name = "meeting_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @EqualsAndHashCode.Exclude
    @NotEmpty
    private List<UserEntity> participants = new ArrayList<>();

}

package github.com.dm_zh.diploma_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserEntity {


    public UserEntity(String id) {
        this.id = id;
    }

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String Login;


    @OneToMany(mappedBy="creator")
    @EqualsAndHashCode.Exclude
    private List<MeetingEntity> createdMeetings = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @EqualsAndHashCode.Exclude
    private List<MeetingEntity> participatedMeetings = new ArrayList<>();

}

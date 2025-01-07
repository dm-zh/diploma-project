package github.com.dm_zh.diploma_project.repository;

import github.com.dm_zh.diploma_project.entity.MeetingEntity;
import github.com.dm_zh.diploma_project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository  extends JpaRepository<UserEntity, String> {
}

package github.com.dm_zh.diploma_project.repository;

import github.com.dm_zh.diploma_project.entity.MeetingEntity;
import github.com.dm_zh.diploma_project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository  extends JpaRepository<MeetingEntity, Integer> {

    @Query("select u from MeetingEntity u join fetch u.participants p where u.creator.id = :userId or p.id = :userId ")
    List<MeetingEntity> findAllByUserId(String userId);
}

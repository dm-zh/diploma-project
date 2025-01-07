package github.com.dm_zh.diploma_project.utils;

import github.com.dm_zh.diploma_project.dto.UserDto;
import github.com.dm_zh.diploma_project.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserUtils {

    public static String getCurrentUserId() {
        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

       return user.getAttribute("sub");
    }

    public static UserDto mapUserEntityToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        return userDto;
    }
}

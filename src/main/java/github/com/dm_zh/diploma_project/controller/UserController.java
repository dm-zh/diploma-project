package github.com.dm_zh.diploma_project.controller;


import github.com.dm_zh.diploma_project.dto.UserDto;
import github.com.dm_zh.diploma_project.repository.UserRepository;
import github.com.dm_zh.diploma_project.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final Service service;

    @GetMapping
    public UserDto getUser() {

        UserDto userDto = new UserDto();
        OAuth2User user = ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        userDto.setId(user.getAttribute("sub"));
        userDto.setEmail(user.getAttribute("email"));
        userDto.setFirstName(user.getAttribute("given_name"));
        userDto.setLastName(user.getAttribute("family_name"));

        service.updateUser(userDto);
        return userDto;
    }

    @GetMapping("/list")
    public List<UserDto> getUsers() {
        return service.getAllUsers();
    }
}

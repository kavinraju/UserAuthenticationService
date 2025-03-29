package com.example.userauthenticationservice.controller;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = authService.signUp(requestDto.getEmailId(), requestDto.getPassword());
        return from(user);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto requestDto) {
        User user = authService.login(requestDto.getEmailId(), requestDto.getPassword());
        return from(user);
    }

    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestBody LogoutRequestDto requestDto) {
        //TODO: To be implemented
        return null;
    }


    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmailId(user.getEmailId());
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}

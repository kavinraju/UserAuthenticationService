package com.example.userauthenticationservice.controller;

import com.example.userauthenticationservice.dtos.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signUp")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto requestDto) {
        return null;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        return null;
    }

    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestBody LogoutRequestDto requestDto) {
        return null;
    }
}

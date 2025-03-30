package com.example.userauthenticationservice.controller;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.UserNotFoundException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import com.mysql.cj.exceptions.PasswordExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.antlr.v4.runtime.misc.Pair;

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
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Pair<User, String> userWithToken = authService.login(requestDto.getEmailId(), requestDto.getPassword());
            UserDto userDto = from(userWithToken.a);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, userWithToken.b);
            return new ResponseEntity<>(userDto, headers, 201);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        } catch (PasswordExpiredException exception) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));

        }
    }

    @PostMapping("/logout")
    public LogoutResponseDto logout(@RequestBody LogoutRequestDto requestDto) {
        //TODO: To be implemented
        return null;
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody ValidateTokenDto validateTokenDto) {
        try {
            Boolean result = authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());
            return new ResponseEntity<>(result, null, 200);
        } catch (Exception exception) {
            return new ResponseEntity<>(false, null, 400);
        }
    }


    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmailId(user.getEmailId());
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}

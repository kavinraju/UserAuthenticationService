package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.InvalidCredentialsException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.exceptions.UserNotFoundException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signUp(String email, String password) {
        Optional<User> optionalUser = userRepo.findUserByEmailId(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("Please try login");
        }

        User user = new User();
        user.setEmailId(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        return userRepo.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepo.findUserByEmailId(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Please signup.");
        }

        String storedPassword = optionalUser.get().getPassword();
        if (!bCryptPasswordEncoder.matches(password, storedPassword)) {
            throw new InvalidCredentialsException("Please enter correct password");
        }
        return optionalUser.get();
    }
}

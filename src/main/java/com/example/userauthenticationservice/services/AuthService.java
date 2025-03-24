package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User signUp(String email, String password) {
        return null;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }
}

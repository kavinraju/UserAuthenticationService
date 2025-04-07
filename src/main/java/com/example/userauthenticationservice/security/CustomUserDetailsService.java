package com.example.userauthenticationservice.security;

import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findUserByEmailId(email);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Username not found: " + email);
        }
        return new CustomUserDetails(optionalUser.get());
    }
}

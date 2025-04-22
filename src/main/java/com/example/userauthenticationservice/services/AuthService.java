package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.clients.KafkaProducerClient;
import com.example.userauthenticationservice.dtos.EmailDto;
import com.example.userauthenticationservice.exceptions.InvalidCredentialsException;
import com.example.userauthenticationservice.exceptions.InvalidTokenException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.exceptions.UserNotFoundException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User signUp(String email, String password) {
        Optional<User> optionalUser = userRepo.findUserByEmailId(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("Please try login");
        }

        User user = new User();
        user.setEmailId(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        // TODO: Send message via kafka
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(user.getEmailId());
        emailDto.setFrom("anuragbatch@gmail.com");
        emailDto.setSubject("Sign up success");
        emailDto.setBody("Thanks for signing. Have a great shopping experience.");

        try {
            kafkaProducerClient.sendMessage("signup", objectMapper.writeValueAsString(emailDto));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return userRepo.save(user);
    }

    @Override
    public Pair<User, String> login(String email, String password) {
        Optional<User> optionalUser = userRepo.findUserByEmailId(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Please signup.");
        }

        String storedPassword = optionalUser.get().getPassword();
        if (!bCryptPasswordEncoder.matches(password, storedPassword)) {
            throw new InvalidCredentialsException("Please enter correct password");
        }

        User user = optionalUser.get();
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        Long currentTime = System.currentTimeMillis();
        payload.put("iat", currentTime);
        payload.put("exp", currentTime + 100000);
        payload.put("iss", "scaler");


        //Generating Token
//        String message = "{\n" +
//                "   \"email\": \"kavinraju@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2025\"\n" + "}";

//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

//        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
//        SecretKey scrSecretKey = macAlgorithm.key().build();

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        return new Pair<>(optionalUser.get(), token);
    }

    //To validate ->
    // - the token which i have received , whether it's matching with any token which is stored in DB
    // - check for expiry by parsing token and getting payload

    //Alternative Implementation Commit - https://github.com/ak-s-0723/UserAuthService_Dec2024/commit/bb2a903fabb0767b641318f3cc27a6ff735692c6
    @Override
    public Boolean validateToken(String token, Long userId) {
        try {
            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();

            String newToken = Jwts.builder().claims(claims).signWith(secretKey).compact();
            if (!token.equals(newToken)) {
                System.out.println(newToken);
                System.out.println(token);
                throw new InvalidTokenException("Token is invalid");
            }

            Long expiry = (Long) claims.get("exp");
            Long currentTime = System.currentTimeMillis();
            if (currentTime > expiry) {
                throw new RuntimeException("Token expired");
            }
            return true;
        } catch (Exception exception) {
            throw exception;
        }

    }
}

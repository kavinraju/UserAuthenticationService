package com.example.userauthenticationservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {
    private String name;
    private String userName;
    private String password;
    private Long phoneNumber;
}

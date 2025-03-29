package com.example.userauthenticationservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class User extends BaseModel {
    private String emailId;
    private String password;

    public User() {
        this.setCreatedTime(new Date());
        this.setLastUpdatedAt(new Date());
    }
}

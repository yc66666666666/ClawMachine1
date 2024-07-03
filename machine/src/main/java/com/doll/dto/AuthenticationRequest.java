package com.doll.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
    private String phone;
    private String code;

    // Getters and setters
}


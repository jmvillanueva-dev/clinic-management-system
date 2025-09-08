package com.clinic.webapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Set<String> roles;
}


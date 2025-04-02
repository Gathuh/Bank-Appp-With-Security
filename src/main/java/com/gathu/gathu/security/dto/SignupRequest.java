package com.gathu.gathu.security.dto;


import com.gathu.gathu.security.entity.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
//    private Role role;
}

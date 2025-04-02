package com.gathu.gathu.security.controller;

import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

@GetMapping("/test")
    public String welcome(){
    return "You are in USER panel";
}
}


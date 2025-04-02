package com.gathu.gathu.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController

@RequestMapping("/test")
//@CrossOrigin(origins = "http://localhost:5173")
public class TestController {

    @GetMapping()
    public String test(){
        return "No role but Logged in";
    }

}

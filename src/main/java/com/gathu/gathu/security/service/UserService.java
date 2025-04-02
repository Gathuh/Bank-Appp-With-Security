package com.gathu.gathu.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.gathu.gathu.security.entity.User;

import java.util.List;

public interface UserService {
    public UserDetailsService userDetailsService();


    User createUser(User user);
    User getUserById(Integer id);
    List<User> getAllUsers();
    User updateUser(Integer id, User user);
    void deleteUser(Integer id);
}

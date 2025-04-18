package com.gathu.gathu.security.repository;

import com.gathu.gathu.security.entity.Role;
import com.gathu.gathu.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public  interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    User findByRole(Role role);

    Optional<User> findByUsername(String username);
    Optional<User> findByVerificationToken(String token); // This method enables findByVerificationToken

}

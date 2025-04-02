package com.gathu.gathu.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

//@Data
@Table(name = "users_table")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uiid;

    @NonNull
    @Column(nullable = false, unique = true)
    private String username;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;




    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    private boolean enabled = true; // Changed to true for simplicity
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean tokenRevoked=false;
    @Column(nullable = false)
    private boolean emailVerified = false; // Tracks if email is verified

    @Column(length = 100)
    private String verificationToken; // Stores the verification token
    @Column(name = "userlogedout")
    private boolean userLogedout;

    private LocalDateTime verificationTokenExpiry;
    public User(String email) {
        if (email == null) {
            throw new RuntimeException("Email Required");
        }
        this.email = email;
    }

    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Default to ROLE_USER
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public boolean isTokenRevoked(){
        return tokenRevoked;}
    public void setTokenRevoked(boolean tokenRevoked){
        this.tokenRevoked=tokenRevoked;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
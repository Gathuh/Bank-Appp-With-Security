package com.gathu.gathu.security.controller;

import com.gathu.gathu.security.dto.AccountRequest;
import com.gathu.gathu.security.dto.CustomerRequest;
import com.gathu.gathu.security.dto.RoleManagementDTO;
import com.gathu.gathu.security.dto.StandingOrderRequest;
import com.gathu.gathu.security.entity.Role;
import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.repository.UserRepository;
import com.gathu.gathu.security.service.AccountService;
import com.gathu.gathu.security.service.CustomerService;
import com.gathu.gathu.security.service.StandingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final StandingOrderService standingOrderService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Great Logged in and in ADMIN panel");
    }

    @PostMapping("/revoke-token")
    public ResponseEntity<String> revokeToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " Not Found!!"));
        user.setTokenRevoked(true);
        userRepository.save(user);
        return ResponseEntity.ok("Tokens revoked for user: " + user.getUsername());
    }

    @PostMapping("/assign-roles")
    public ResponseEntity<String> assignRoles(@RequestBody RoleManagementDTO roleManagementDTO) {
        User user = userRepository.findByEmail(roleManagementDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + roleManagementDTO.getEmail() + " Not Found!!"));
        try {
            user.setRole(Role.valueOf(roleManagementDTO.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + roleManagementDTO.getRole() + ". Must be 'USER' or 'ADMIN'.");
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Role assigned successfully. " + user.getUsername() + " is now an " + user.getRole() + ". You can double check that the email set is " + user.getEmail());
    }

    @PostMapping("/customers")
    public ResponseEntity<String> onboardCustomer(@RequestBody CustomerRequest customerRequest) {
        customerService.onboardCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Customer onboarded successfully: " + customerRequest.getEmail());
    }

    @PostMapping("/accounts")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest) {
        accountService.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created successfully for customer ID: " + accountRequest.getCustomerId());
    }

    @PostMapping("/standing-orders")
    public ResponseEntity<String> createStandingOrder(@RequestBody StandingOrderRequest standingOrderRequest) {
        standingOrderService.createStandingOrder(standingOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Standing order created successfully from account " + standingOrderRequest.getSourceAccountId() + " to " + standingOrderRequest.getDestinationAccountId());
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
      List<User>  user= userRepository.findAll();

      return    ResponseEntity.status(HttpStatus.OK).body(user.stream().collect(Collectors.toList()));

    }
}
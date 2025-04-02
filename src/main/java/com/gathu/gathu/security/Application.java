package com.gathu.gathu.security;

import com.gathu.gathu.security.entity.Role;
import com.gathu.gathu.security.entity.User;
import com.gathu.gathu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application implements CommandLineRunner {
@Autowired
private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Check if an ADMIN user already exists
		User adminAccount = userRepository.findByRole(Role.ADMIN);

		if (adminAccount == null) {
			User user = new User();
			// Required fields (non-null)
			user.setEmail("admin@nyati.co.ke");
			user.setUsername("ADMIN");
			user.setPassword(new BCryptPasswordEncoder().encode("Admin"));
			user.setRole(Role.ADMIN);

			// Optional fields (but setting them to avoid nulls)
			user.setFirstName("Admin");
			user.setLastName("User"); // Set a default last name to avoid null

			// Security fields (non-null, default to true)
			user.setEnabled(true);
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);

			// Save the user
			userRepository.save(user);
			System.out.println("Initial ADMIN user created: admin@nyati.co.ke with password: Admin");
		} else {
			System.out.println("ADMIN user already exists: " + adminAccount.getEmail());
		}
	}
}

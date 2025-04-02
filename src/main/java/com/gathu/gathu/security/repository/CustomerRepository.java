//package com.gathu.gathu.security.repository;
//
//import com.gathu.gathu.security.entity.Customer;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    Optional<Customer> findByEmail(String email);
//    Optional<Customer> findByUserUiid(Long userUiid); // Changed from findByUserId to findByUserUiid
//}

package com.gathu.gathu.security.repository;

import com.gathu.gathu.security.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.user.uiid = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);
}
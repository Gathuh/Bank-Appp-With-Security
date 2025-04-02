package com.gathu.gathu.security.repository;

import com.gathu.gathu.security.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
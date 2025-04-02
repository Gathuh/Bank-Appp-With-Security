package com.gathu.gathu.security.service;

import com.gathu.gathu.security.dto.AccountRequest;
import com.gathu.gathu.security.entity.Account;
import com.gathu.gathu.security.entity.Customer;
import com.gathu.gathu.security.repository.AccountRepository;
import com.gathu.gathu.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public Account createAccount(AccountRequest request) {
        Customer customer = customerRepository.findByUserId(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for User ID: " + request.getCustomerId()));

        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountType(request.getAccountType());
        return accountRepository.save(account);
    }

    public void debitAccount(Account account, Double amount) {
        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance in account: " + account.getId());
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    public void creditAccount(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
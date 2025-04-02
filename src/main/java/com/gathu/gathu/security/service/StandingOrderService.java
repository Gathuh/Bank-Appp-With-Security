package com.gathu.gathu.security.service;

import com.gathu.gathu.security.dto.StandingOrderRequest;
import com.gathu.gathu.security.entity.Account;
import com.gathu.gathu.security.entity.StandingOrder;
import com.gathu.gathu.security.repository.AccountRepository;
import com.gathu.gathu.security.repository.StandingOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StandingOrderService {

    private final StandingOrderRepository standingOrderRepository;
    private final AccountRepository accountRepository;

    public StandingOrder createStandingOrder(StandingOrderRequest request) {
        Account sourceAccount = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Source account not found with ID: " + request.getSourceAccountId()));
        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found with ID: " + request.getDestinationAccountId()));

        StandingOrder standingOrder = new StandingOrder();
        standingOrder.setSourceAccount(sourceAccount);
        standingOrder.setDestinationAccount(destinationAccount);
        standingOrder.setAmount(request.getAmount());
        standingOrder.setSchedule(request.getSchedule());
        standingOrder.setNextExecution(LocalDateTime.now().plusDays(1)); // Example: Next execution tomorrow
        return standingOrderRepository.save(standingOrder);
    }
}
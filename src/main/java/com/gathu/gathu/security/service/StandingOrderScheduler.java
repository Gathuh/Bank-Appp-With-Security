package com.gathu.gathu.security.service;

import com.gathu.gathu.security.entity.Account;
import com.gathu.gathu.security.entity.StandingOrder;
import com.gathu.gathu.security.repository.StandingOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StandingOrderScheduler {

    private final StandingOrderRepository standingOrderRepository;
    private final AccountService accountService;

    @Scheduled(fixedRate = 60000) // Runs every minute for testing; adjust as needed
    @Transactional
    public void executeStandingOrders() {
        LocalDateTime now = LocalDateTime.now();
        List<StandingOrder> dueOrders = standingOrderRepository.findByNextExecutionBefore(now);

        for (StandingOrder order : dueOrders) {
            try {
                Account source = order.getSourceAccount();
                Account destination = order.getDestinationAccount();

                accountService.debitAccount(source, order.getAmount());
                accountService.creditAccount(destination, order.getAmount());

                // Update execution times (simplified: next execution +1 day)
                order.setLastExecuted(now);
                order.setNextExecution(now.plusDays(1));
                standingOrderRepository.save(order);
            } catch (Exception e) {
                // Log error (in a real app, use a proper logging framework)
                System.err.println("Failed to execute standing order " + order.getId() + ": " + e.getMessage());
            }
        }
    }
}
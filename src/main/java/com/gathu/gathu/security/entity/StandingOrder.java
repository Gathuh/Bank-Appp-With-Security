package com.gathu.gathu.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "standing_orders")
@Getter
@Setter
public class StandingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String schedule; // e.g., "DAILY", "WEEKLY", "MONTHLY"

    @Column(name = "last_executed")
    private LocalDateTime lastExecuted;

    @Column(name = "next_execution", nullable = false)
    private LocalDateTime nextExecution;
}
package com.gathu.gathu.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandingOrderRequest {
    private Long sourceAccountId;
    private Long destinationAccountId;
    private Double amount;
    private String schedule;
}
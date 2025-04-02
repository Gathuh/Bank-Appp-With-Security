package com.gathu.gathu.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private Long customerId;
    private String accountType;
}
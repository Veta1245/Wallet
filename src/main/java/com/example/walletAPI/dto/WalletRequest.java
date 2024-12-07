package com.example.walletAPI.dto;

import com.example.walletAPI.enam.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;

}

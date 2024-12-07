package com.example.walletAPI.mapper;

import com.example.walletAPI.dto.WalletDTO;
import com.example.walletAPI.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletMapper {

    public WalletDTO toWalletDto(Wallet wallet) {
        return new WalletDTO(wallet.getWalletId(), new BigDecimal(wallet.getAmount()));
    }
}

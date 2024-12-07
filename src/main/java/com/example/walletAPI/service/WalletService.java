package com.example.walletAPI.service;

import com.example.walletAPI.dto.WalletDTO;
import com.example.walletAPI.dto.WalletRequest;

import java.util.UUID;

public interface WalletService {
    WalletDTO makeADeposit(WalletRequest walletRequest);

    WalletDTO getData(UUID uuid);
}

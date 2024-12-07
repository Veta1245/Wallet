package com.example.walletAPI.controllers;

import com.example.walletAPI.dto.WalletDTO;
import com.example.walletAPI.dto.WalletRequest;
import com.example.walletAPI.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/wallet")
    public WalletDTO makeADeposit(@Valid @RequestBody WalletRequest walletRequest) {
        return walletService.makeADeposit(walletRequest);
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public WalletDTO getData(@PathVariable UUID WALLET_UUID) {
        return walletService.getData(WALLET_UUID);
    }
}

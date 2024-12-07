package com.example.walletAPI.service;

import com.example.walletAPI.dto.WalletDTO;
import com.example.walletAPI.dto.WalletRequest;
import com.example.walletAPI.entity.Wallet;
import com.example.walletAPI.exception.modelException.IncorrectDataException;
import com.example.walletAPI.exception.modelException.NotFoundException;
import com.example.walletAPI.mapper.WalletMapper;
import com.example.walletAPI.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.example.walletAPI.enam.OperationType.DEPOSIT;
import static com.example.walletAPI.enam.OperationType.WITHDRAW;
import static java.math.BigDecimal.ZERO;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository wallRepos;
    private final WalletMapper walletMapper;

    @Override
    @Transactional
    public WalletDTO makeADeposit(WalletRequest walletRequest) {
        log.info("Поступили данные : {}", walletRequest);
        Wallet wallet = findWallet(walletRequest.getWalletId());
        if (DEPOSIT.equals(walletRequest.getOperationType())) {
            wallet.setAmount(walletRequest.getAmount().add(new BigDecimal(wallet.getAmount())).toPlainString());
            log.info("Внесен депозит: {}", wallet);
        }
        if (WITHDRAW.equals(walletRequest.getOperationType())) {
            if (new BigDecimal(wallet.getAmount()).subtract(walletRequest.getAmount()).compareTo(ZERO) < 0) {
                throw new IncorrectDataException("Недостаточно средств для снятия со счета");
            } else {
                wallet.setAmount(new BigDecimal(wallet.getAmount()).subtract(walletRequest.getAmount()).toPlainString());
                log.info("Снятие со счета: {}", wallet);
            }
        }
        return walletMapper.toWalletDto(wallet);
    }


    @Override
    @Transactional
    public WalletDTO getData(UUID uuid) {
        Wallet wallet = findWallet(uuid);
        log.info("Получение данных: {}", wallet);
        return walletMapper.toWalletDto(wallet);
    }

    private Wallet findWallet(UUID walletId) {
        Optional<Wallet> wallet = wallRepos.findByWalletId(walletId);
        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new NotFoundException("Кошелёк не найден");
        }
    }
}

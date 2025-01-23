package com.selyunina.wallet.service;

import com.selyunina.wallet.domain.OperationType;
import com.selyunina.wallet.domain.Wallet;
import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;
import com.selyunina.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletDto create() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        walletRepository.save(wallet);

        return WalletDto.from(wallet);
    }

    @Override
    public WalletDto updateBalance(WalletRequestDto request) {
        Wallet wallet = findById(request.walletId());

        BigDecimal newBalance;
        if (request.operationType().equals(OperationType.DEPOSIT)) {
            newBalance = wallet.getBalance().add(request.amount());
        } else {
            newBalance = wallet.getBalance().subtract(request.amount());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("ljf");
            }
        }
        wallet.setBalance(newBalance);

        walletRepository.save(wallet);
        return WalletDto.from(wallet);
    }

    @Override
    public WalletDto getBalance(UUID id) {
        Wallet wallet = findById(id);
        return WalletDto.from(wallet);
    }

    private Wallet findById(UUID id) {
        return walletRepository.findById(id).orElseThrow();
    }
}

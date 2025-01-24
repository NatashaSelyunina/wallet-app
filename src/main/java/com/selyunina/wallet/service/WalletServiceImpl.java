package com.selyunina.wallet.service;

import com.selyunina.wallet.domain.Wallet;
import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;
import com.selyunina.wallet.exception_handling.IncorrectInformationException;
import com.selyunina.wallet.repository.WalletRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
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
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    @Transactional
    public WalletDto updateBalance(WalletRequestDto request) {
        Wallet wallet = findById(request.walletId());

        BigDecimal newBalance;
        switch (request.operationType()) {
            case DEPOSIT -> newBalance = wallet.getBalance().add(request.amount());
            case WITHDRAW -> {
                newBalance = wallet.getBalance().subtract(request.amount());
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IncorrectInformationException(
                            "The balance cannot be less than zero. You have entered an amount that exceeds the account balance",
                            HttpStatus.BAD_REQUEST);
                }
            }
            default -> throw new RuntimeException("Invalid operationType");
        }
        wallet.setBalance(newBalance);

        walletRepository.save(wallet);
        return WalletDto.from(wallet);
    }

    @Override
    @Cacheable(value = "wallets", key = "#id")
    public WalletDto getBalance(UUID id) {
        Wallet wallet = findById(id);
        return WalletDto.from(wallet);
    }

    private Wallet findById(@NonNull UUID id) {
        return walletRepository.findByIdWithLock(id, LockModeType.PESSIMISTIC_WRITE).orElseThrow(() ->
                new IncorrectInformationException("Wallet with id " + id + " not found", HttpStatus.BAD_REQUEST));
    }
}

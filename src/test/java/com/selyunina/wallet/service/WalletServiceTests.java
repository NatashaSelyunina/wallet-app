package com.selyunina.wallet.service;

import com.selyunina.wallet.domain.OperationType;
import com.selyunina.wallet.domain.Wallet;
import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;
import com.selyunina.wallet.exception_handling.IncorrectInformationException;
import com.selyunina.wallet.repository.WalletRepository;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalletService is work: ")
public class WalletServiceTests {

    @Mock
    private WalletRepository walletRepository;

    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(walletRepository);
    }

    @Test
    void testCreateWalletPositive() {
        Wallet wallet = createWallet(UUID.randomUUID(), BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        assertDoesNotThrow(() -> walletService.create());

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testGetBalancePositive() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = createWallet(walletId, BigDecimal.valueOf(200));

        when(walletRepository.findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(Optional.of(wallet));
        WalletDto result = walletService.getBalance(walletId);

        assertEquals(BigDecimal.valueOf(200), result.balance());
        verify(walletRepository, times(1)).findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    void testGetBalanceNegative() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = createWallet(walletId, BigDecimal.valueOf(4000));

        IncorrectInformationException exception = assertThrows(IncorrectInformationException.class,
                () -> walletService.getBalance(walletId));
        assertEquals("Wallet with id " + walletId  + " not found", exception.getMessage());

        verify(walletRepository, times(1)).findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    void testUpdateBalancePositive() {
        UUID walletId = UUID.randomUUID();
        WalletRequestDto requestDto = new WalletRequestDto(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(500));
        Wallet wallet = createWallet(walletId, BigDecimal.valueOf(4000));

        when(walletRepository.findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(Optional.of(wallet));

        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            return invocation.<Wallet>getArgument(0);
        });

        WalletDto result = walletService.updateBalance(requestDto);

        assertEquals(BigDecimal.valueOf(4500), result.balance());
        verify(walletRepository, times(1)).save(wallet);
        verify(walletRepository, times(1)).findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    void testUpdateBalanceNegative_WalletNotFound() {
        UUID walletId = UUID.randomUUID();
        WalletRequestDto requestDto = new WalletRequestDto(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(500));
        createWallet(walletId, BigDecimal.valueOf(4000));

        IncorrectInformationException exception = assertThrows(IncorrectInformationException.class,
                () -> walletService.updateBalance(requestDto));
        assertEquals("Wallet with id " + walletId  + " not found", exception.getMessage());

        verify(walletRepository, times(1)).findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    void testUpdateBalanceNegative_SubzeroBalance() {
        UUID walletId = UUID.randomUUID();
        WalletRequestDto requestDto = new WalletRequestDto(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(4000));
        Wallet wallet = createWallet(walletId, BigDecimal.valueOf(500));

        when(walletRepository.findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(Optional.of(wallet));

        IncorrectInformationException exception = assertThrows(IncorrectInformationException.class,
                () -> walletService.updateBalance(requestDto));
        assertEquals("The balance cannot be less than zero. You have entered an amount that exceeds the account balance",
                exception.getMessage());

        verify(walletRepository, times(1)).findByIdWithLock(walletId, LockModeType.PESSIMISTIC_WRITE);
    }

    private Wallet createWallet(UUID walletId, BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(balance);
        return wallet;
    }
}

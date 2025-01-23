package com.selyunina.wallet.service;

import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    @Override
    public WalletDto create() {
        return null;
    }

    @Override
    public WalletDto updateBalance(WalletRequestDto request) {
        return null;
    }

    @Override
    public WalletDto getBalance(UUID id) {
        return null;
    }
}

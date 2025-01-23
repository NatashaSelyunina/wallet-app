package com.selyunina.wallet.service;

import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;

import java.util.UUID;

public interface WalletService {

    WalletDto create();

    WalletDto updateBalance(WalletRequestDto request);

    WalletDto getBalance(UUID id);
}

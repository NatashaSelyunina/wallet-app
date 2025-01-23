package com.selyunina.wallet.dto;

import com.selyunina.wallet.domain.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(

        UUID id,

        BigDecimal balance
) {

    public static WalletDto from(Wallet wallet) {
        return new WalletDto(
                wallet.getId(),
                wallet.getBalance()
        );
    }
}

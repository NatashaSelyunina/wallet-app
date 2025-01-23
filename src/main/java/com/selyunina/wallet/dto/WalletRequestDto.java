package com.selyunina.wallet.dto;

import com.selyunina.wallet.domain.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletRequestDto(

        @NotNull
        UUID walletId,

        @NotNull
        OperationType operationType,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount
) {
}

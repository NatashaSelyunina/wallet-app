package com.selyunina.wallet.dto;

import com.selyunina.wallet.domain.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletRequestDto(

        @NotNull(message = "The wallet ID is mandatory")
        UUID walletId,

        @NotNull(message = "The type of operation is mandatory")
        OperationType operationType,

        @NotNull(message = "The amount you want to deposit/withdraw from your account is mandatory")
        @DecimalMin(value = "0.01", message = "The amount you want to deposit/withdraw from the account must be greater than 0")
        BigDecimal amount
) {
}

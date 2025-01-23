package com.selyunina.wallet.repository;

import com.selyunina.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}

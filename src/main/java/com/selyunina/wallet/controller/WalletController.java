package com.selyunina.wallet.controller;

import com.selyunina.wallet.dto.WalletDto;
import com.selyunina.wallet.dto.WalletRequestDto;
import com.selyunina.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<WalletDto> create() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletService.create());
    }

    @PostMapping("/update")
    public ResponseEntity<WalletDto> updateBalance(@Valid @RequestBody WalletRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(walletService.updateBalance(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getBalance(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(walletService.getBalance(id));
    }
}

package com.smartmerge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartmerge.model.Account;
import com.smartmerge.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class Test {

    private final AccountRepository accountRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/getTestAccount")
    public ResponseEntity<Account> getTestAccount() {
        return ResponseEntity.ok(accountRepository.findById(12345).get());
    }
}

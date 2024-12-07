package com.example.walletAPI.exception.modelException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
        log.error(message);
    }
}

package com.example.walletAPI.exception.controllerException;

import com.example.walletAPI.exception.dtoException.ErrorResponse;
import com.example.walletAPI.exception.modelException.IncorrectDataException;
import com.example.walletAPI.exception.modelException.NotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerException {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exception) {
        return new ErrorResponse(LocalDateTime.now(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIncorrectDataException(final IncorrectDataException exception) {
        return new ErrorResponse(LocalDateTime.now(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        return new ErrorResponse(LocalDateTime.now(), "Невалидный JSON файл");
    }
}

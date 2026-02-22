package com.hospito.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class HospitoException extends RuntimeException{
    private final HttpStatus status;
    public HospitoException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}

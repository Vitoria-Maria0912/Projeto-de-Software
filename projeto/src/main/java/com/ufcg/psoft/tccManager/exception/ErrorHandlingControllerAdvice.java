package com.ufcg.psoft.tccManager.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private TccErrorType defaultTccErrorTypeConstruct(String message) {
        return TccErrorType.builder()
                .timestamp(LocalDateTime.now())
                .errors(new ArrayList<>())
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public TccErrorType onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        TccErrorType tccErrorType = defaultTccErrorTypeConstruct(
                "Erros de validacao encontrados"
        );
        for(FieldError fieldError: e.getBindingResult().getFieldErrors()) {
            tccErrorType.getErrors().add(fieldError.getDefaultMessage());
        }
        return tccErrorType;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public TccErrorType onConstraintViolation(ConstraintViolationException e) {
        TccErrorType tccErrorType = defaultTccErrorTypeConstruct(
                "Erros de validação encontrados"
        );
        for(ConstraintViolation<?> violation: e.getConstraintViolations()) {
            tccErrorType.getErrors().add(violation.getMessage());
        }
        return tccErrorType;
    }

    @ExceptionHandler(TccException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public TccErrorType onTccMatchException(TccException e) {
        return defaultTccErrorTypeConstruct(
                e.getMessage()
        );
    }

}
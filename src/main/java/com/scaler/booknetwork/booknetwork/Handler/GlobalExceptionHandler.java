package com.scaler.booknetwork.booknetwork.Handler;


import com.scaler.booknetwork.booknetwork.Request.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.OperationNotSupportedException;
import javax.security.auth.login.AccountLockedException;

import java.util.Map;

import static com.scaler.booknetwork.booknetwork.Models.BusinessErrorCode.ACCOUNT_DISABLE;
import static com.scaler.booknetwork.booknetwork.Models.BusinessErrorCode.ACCOUNT_LOCKED;
import static org.ietf.jgss.GSSException.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException e) {
        Map<String, String> errorMap = Map.of("error", e.getMessage());
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_LOCKED.getCode())
                                .businessErrorMessage(ACCOUNT_LOCKED.getMessage())
                                .errors(errorMap)  // Pass a map here
                                .build()
                );
    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(DisabledException e) {
        Map<String, String> errorMap = Map.of("error", e.getMessage());
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_DISABLE.getCode())
                                .businessErrorMessage(ACCOUNT_DISABLE.getMessage())
                                .errors(errorMap)  // Pass a map here
                                .build()
                );
    }

    @ExceptionHandler(OperationNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(OperationNotSupportedException e) {
        Map<String, String> errorMap = Map.of("error", e.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .errors(errorMap)  // Pass a map here
                                .build()
                );
    }

}

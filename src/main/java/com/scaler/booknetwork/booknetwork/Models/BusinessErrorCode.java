package com.scaler.booknetwork.booknetwork.Models;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCode {

    NO_CODE(0,NOT_IMPLEMENTED,"NO CODE"),
    INCORRECT_CURRENT_PASSWORD(300,BAD_REQUEST,"CURRENT PASSWORD IS INCORRECT"),
    NEW_PASSWORD_DOES_NOT_MATCH(301,BAD_REQUEST,"PASSWORD DOES NOT MATCH"),
    ACCOUNT_LOCKED(302, FORBIDDEN,"ACCOUNT LOCKED"),
    ACCOUNT_DISABLE(310, FORBIDDEN,"ACCOUNT DISABLED"),
    BAD_CREDENTIALS(400, BAD_REQUEST,"CREDENTIALS ISSUE");


    @Getter
    private Integer code;
    @Getter
    private String message;
    @Getter
    private HttpStatus httpStatus;
    private BusinessErrorCode(Integer code, HttpStatus status, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = status;

    }
}

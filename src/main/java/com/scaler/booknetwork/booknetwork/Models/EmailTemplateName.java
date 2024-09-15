package com.scaler.booknetwork.booknetwork.Models;


import lombok.Getter;

@Getter

public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_Account");

    private String value;
    EmailTemplateName(String value) {
        this.value = value;
    }
}

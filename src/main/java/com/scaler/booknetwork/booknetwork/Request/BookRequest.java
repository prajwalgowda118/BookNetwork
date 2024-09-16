package com.scaler.booknetwork.booknetwork.Request;


import jakarta.validation.constraints.NotNull;
import lombok.*;



public record BookRequest (
        Integer id,
        @NotNull(message="100")
        String Title,
        @NotNull(message="100")
        String AuthorName,
        String isbn,
        String Synopsis,
        boolean sharable) {




}

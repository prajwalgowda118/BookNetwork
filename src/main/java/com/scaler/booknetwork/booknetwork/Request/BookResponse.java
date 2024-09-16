package com.scaler.booknetwork.booknetwork.Request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class BookResponse {


    private Integer bookId;
    private String title;
    private String author;
    private String synopsis;
    private String isbn;
    private String owner;
    private double rate;
    private boolean archived;
    private boolean shareable;
    private byte[] coverImage;


}

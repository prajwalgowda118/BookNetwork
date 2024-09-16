package com.scaler.booknetwork.booknetwork.Request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {


    private Integer bookId;
    private String title;
    private String author;
    private String synopsis;
    private String isbn;
    private String owner;
    private double rate;
    private boolean returned;
     private boolean returnApproved;
}

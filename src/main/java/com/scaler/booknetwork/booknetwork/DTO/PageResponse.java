package com.scaler.booknetwork.booknetwork.DTO;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PageResponse <T>{


    private List<T> content;
    private int number;
    private int size;
    private Long totalElements;
    private boolean first;
    private boolean last;
}

package com.scaler.booknetwork.booknetwork.DTO;

import com.scaler.booknetwork.booknetwork.Models.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {


    public static Specification<Book> withOwnerID(Integer id) {

        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), id));

    }
}

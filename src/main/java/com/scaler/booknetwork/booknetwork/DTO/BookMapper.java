package com.scaler.booknetwork.booknetwork.DTO;


import com.scaler.booknetwork.booknetwork.Config.FileUtils;
import com.scaler.booknetwork.booknetwork.Models.Book;
import com.scaler.booknetwork.booknetwork.Models.BookTransactionHistory;
import com.scaler.booknetwork.booknetwork.Request.BookRequest;
import com.scaler.booknetwork.booknetwork.Request.BookResponse;
import com.scaler.booknetwork.booknetwork.Request.BorrowedBookResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {



    public Book toBook(BookRequest bookRequest){

        return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.Title())
                .author(bookRequest.AuthorName())
                .synopsis(bookRequest.Synopsis())
                .archived(false)
                .shareable(bookRequest.sharable()).build();
    }

    public BookResponse toBookResponse(Book book) {
        byte[] coverImage = null;
        if (!StringUtils.isEmpty(book.getBookCover())) {
            coverImage = FileUtils.readFile(book.getBookCover());
        }

        return BookResponse.builder()
                .bookId((int) book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(String.valueOf(book.getOwner().fullName()))
                .author(book.getAuthor())
                .coverImage(coverImage)  // Can be null if file reading fails
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history){

        return BorrowedBookResponse.builder()
                .bookId((int) history.getBook().getId())
                .title(history.getBook().getTitle())
                .author(history.getBook().getAuthor())
                .isbn(history.getBook().getIsbn())
                .synopsis(history.getBook().getSynopsis())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();

    }
}

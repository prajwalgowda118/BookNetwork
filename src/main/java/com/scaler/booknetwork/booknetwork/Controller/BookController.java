package com.scaler.booknetwork.booknetwork.Controller;

import com.scaler.booknetwork.booknetwork.DTO.PageResponse;
import com.scaler.booknetwork.booknetwork.Models.Book;
import com.scaler.booknetwork.booknetwork.Models.User;
import com.scaler.booknetwork.booknetwork.Request.BookRequest;
import com.scaler.booknetwork.booknetwork.Request.BookResponse;
import com.scaler.booknetwork.booknetwork.Request.BorrowedBookResponse;
import com.scaler.booknetwork.booknetwork.Service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Tag(name ="book")
public class BookController {


    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> CreateBook(@RequestBody BookRequest request,
                                                       Authentication connectedUser) {
           // User user = (User) connectedUser.;
            return ResponseEntity.ok(bookService.CreateBook(request, connectedUser));
    }

    @GetMapping("/{bookID}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Integer bookID) {


        return ResponseEntity.ok(bookService.GetBookById((int) bookID));

    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
            @RequestParam (name ="page", defaultValue = "0", required = false) int page,
            @RequestParam (name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(bookService.findAllBooks(page,size,connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getAllBookByOwner(
            @RequestParam (name ="page", defaultValue = "0", required = false) int page,
            @RequestParam (name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllBookByBorrowed(
            @RequestParam (name ="page", defaultValue = "0", required = false) int page,
            @RequestParam (name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(bookService.findAllBorrowedBook(page,size,connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getAllReturnedBooks(
            @RequestParam (name ="page", defaultValue = "0", required = false) int page,
            @RequestParam (name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(bookService.findAllReturnedBook(page,size,connectedUser));
    }


    @PatchMapping("/shareable/{bookId}")
    public ResponseEntity<Integer> updateShareableStatus
            (
                    @PathVariable("bookId") Integer bookId,
                    Authentication authenticatedUser
            ) throws OperationNotSupportedException {

      return  ResponseEntity.ok(bookService.updateShareableStatus(bookId,authenticatedUser));
    }

    @PatchMapping("/archived/{bookId}")
    public ResponseEntity<Integer> updateArchivedStatus
            (
                    @PathVariable("bookId") Integer bookId,
                    Authentication authenticatedUser
            ) throws OperationNotSupportedException {

        return  ResponseEntity.ok(bookService.updateArchivedStatus(bookId,authenticatedUser));
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<Integer> borrowBook
            (
                    @PathVariable("bookId") Integer bookId,
                    Authentication authenticatedUser
            ) throws OperationNotSupportedException {

        return  ResponseEntity.ok(bookService.borrowBook(bookId,authenticatedUser));
    }


    @PostMapping("/borrow/return/{bookId}")
    public ResponseEntity<Integer> returnBorrowBook
            (
                    @PathVariable("bookId") Integer bookId,
                    Authentication authenticatedUser
            ) throws OperationNotSupportedException {

        return  ResponseEntity.ok(bookService.returnBorrowBook(bookId,authenticatedUser));
    }

    @PostMapping("/borrow/return/aprrove/{bookId}")
    public ResponseEntity<Integer> approveReturnBorrowBook
            (
                    @PathVariable("bookId") Integer bookId,
                    Authentication authenticatedUser
            ) throws OperationNotSupportedException {

        return  ResponseEntity.ok(bookService.approveReturnBorrowBook(bookId,authenticatedUser));
    }


    @PostMapping(value="/cover/{bookId}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(

            @PathVariable("bookId") Integer bookId,
            Authentication authenticatedUser,
            //@Parameter(name = "", value = ""),
            @RequestPart("file") MultipartFile file
            ){

            bookService.uploadBookCoverPicture(bookId,authenticatedUser,file);
        return ResponseEntity.accepted().build();


    }


}

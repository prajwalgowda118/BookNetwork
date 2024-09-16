package com.scaler.booknetwork.booknetwork.Service;

import com.scaler.booknetwork.booknetwork.DTO.BookMapper;
import com.scaler.booknetwork.booknetwork.DTO.BookSpecification;
import com.scaler.booknetwork.booknetwork.DTO.PageResponse;
import com.scaler.booknetwork.booknetwork.Models.Book;
import com.scaler.booknetwork.booknetwork.Models.BookTransactionHistory;
import com.scaler.booknetwork.booknetwork.Models.User;
import com.scaler.booknetwork.booknetwork.Repository.BookRepository;
import com.scaler.booknetwork.booknetwork.Repository.BookTransactionHistoryRepository;
import com.scaler.booknetwork.booknetwork.Repository.UserRepository;
import com.scaler.booknetwork.booknetwork.Request.BookRequest;
import com.scaler.booknetwork.booknetwork.Request.BookResponse;
import com.scaler.booknetwork.booknetwork.Request.BorrowedBookResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

//import java.awt.print.Pageable;
import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor

public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer CreateBook(BookRequest request, Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return Math.toIntExact(bookRepository.save(book).getId());
    }

    public BookResponse GetBookById(Integer id) {

        return bookRepository.findById(Long.valueOf(id))
                .map(bookMapper::toBookResponse)
                .orElseThrow(()-> new EntityNotFoundException("no book found for the id "+id));

    }

    public PageResponse<BookResponse> findAllBooks( int page, int size, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("createdAt").descending());

        Page<Book> books = bookRepository.findAllDisplayableBooks( pageable,user.getId());

        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("createdAt").descending());

        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerID(Math.toIntExact(user.getId())), pageable);

        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBook(int page, int size, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("createdAt").descending());


        Page<BookTransactionHistory> allBorrowedBooks =
                bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());

        List<BorrowedBookResponse> bookResponses
                             =allBorrowedBooks.stream()
                                .map(bookMapper::toBorrowedBookResponse)
                                  .toList();


         return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBook(int page, int size, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<BookTransactionHistory> allReturnedBooks =
                bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());

        List<BorrowedBookResponse> bookResponses = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication authenticatedUser) throws OperationNotSupportedException {

        Book book=bookRepository.findById(Long.valueOf(bookId)).orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));
        User user=(User) authenticatedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId(),user.getId())) {
            throw  new OperationNotSupportedException("you cannot update book sharable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication authenticatedUser) throws OperationNotSupportedException {

        Book book=bookRepository.findById(Long.valueOf(bookId))
                            .orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));
        User user=(User) authenticatedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId(),user.getId())) {
            throw  new OperationNotSupportedException("you cannot update book archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication authenticatedUser) throws OperationNotSupportedException {

        User user=(User) authenticatedUser.getPrincipal();
        Book book=bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));

        if(book.isArchived() ||!book.isShareable()){

            throw new OperationNotSupportedException("the requested book cannot be borrowed");
        }

        if(book.getOwner().getId().equals(user.getId())) {
            throw  new OperationNotSupportedException("you cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed =
                bookTransactionHistoryRepository.isAlreadyBorrowedByUser(Long.valueOf(bookId),user.getId());

        if(isAlreadyBorrowed) {
            throw  new OperationNotSupportedException("you cannot borrow your own book");
        }

        BookTransactionHistory bookTransactionHistory=
                    BookTransactionHistory
                            .builder()
                            .user(user)
                            .book(book)
                            .returnApproved(false)
                            .returned(false)
                             .build();

       return Math.toIntExact((bookTransactionHistoryRepository.save(bookTransactionHistory).getId()));
    }

    public Integer returnBorrowBook(Integer bookId, Authentication authenticatedUser) throws OperationNotSupportedException {

        User user=(User) authenticatedUser.getPrincipal();
        Book book=bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));
        if(book.isArchived() ||!book.isShareable()){

            throw new OperationNotSupportedException("the requested book cannot be borrowed");
        }

        if(book.getOwner().getId().equals(user.getId())) {
            throw  new OperationNotSupportedException("you cannot borrow/return your own book");
        }

        BookTransactionHistory bookTransactionHistory
                        =bookTransactionHistoryRepository.findByBookIdAndUserIdAndReturnApprovedFalse(bookId,user.getId());
        if(bookTransactionHistory == null) {
            throw  new OperationNotSupportedException("you didnot borrowed the book to return ");
        }

        bookTransactionHistory.setReturnApproved(true);
        bookTransactionHistoryRepository.save(bookTransactionHistory);

        return Math.toIntExact(bookTransactionHistoryRepository.save(bookTransactionHistory).getId());

    }

    public Integer approveReturnBorrowBook(Integer bookId, Authentication authenticatedUser) throws OperationNotSupportedException {
        
        User user=(User) authenticatedUser.getPrincipal();
        Book book=bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));
        if(book.isArchived() ||!book.isShareable()){

            throw new OperationNotSupportedException("the requested book cannot be borrowed");
        }

        if(book.getOwner().getId().equals(user.getId())) {
            throw  new OperationNotSupportedException("you cannot borrow/return your own book");
        }

        BookTransactionHistory bookTransactionHistory
                =bookTransactionHistoryRepository.findByBookIdAndCreatedByAndReturnApprovedFalseAndReturnedTrue(bookId,user.getId());
        if(bookTransactionHistory == null) {
            throw  new OperationNotSupportedException("book not returned yet,you cannot approve its return ");
        }

        bookTransactionHistory.setReturnApproved(true);
        bookTransactionHistoryRepository.save(bookTransactionHistory);
        return Math.toIntExact(bookTransactionHistoryRepository.save(bookTransactionHistory).getId());

    }

    public void uploadBookCoverPicture(Integer bookId, Authentication authenticatedUser, MultipartFile file) {

        Book book=bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(()-> new EntityNotFoundException("no book found with the id"+ bookId));
        User user=(User) authenticatedUser.getPrincipal();

        var bookCover =fileStorageService.saveFile(file,book,user.getId());

        book.setBookCover(bookCover);
        bookRepository.save(book);

    }
}

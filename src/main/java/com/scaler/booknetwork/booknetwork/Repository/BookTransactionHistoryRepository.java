package com.scaler.booknetwork.booknetwork.Repository;

import com.scaler.booknetwork.booknetwork.Models.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("select history from BookTransactionHistory history where history.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, @Param("userId") Long userId);

    @Query("""
    select history from BookTransactionHistory history
    where history.book.id = :bookId
    and history.returned = true
    """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, @Param("bookId") Long bookId);

    @Query("""
        select (count(history) > 0) from BookTransactionHistory history
        where history.user.id = :userId
        and history.book.id = :bookId
    """)
    boolean isAlreadyBorrowedByUser(@Param("bookId") Long bookId, @Param("userId") Long userId);


    BookTransactionHistory findByBookIdAndUserIdAndReturnApprovedFalse(Integer bookId, Long userId);

    BookTransactionHistory findByBookIdAndCreatedByAndReturnApprovedTrue(Integer bookId, Long id);

    BookTransactionHistory findByBookIdAndCreatedByAndReturnApprovedFalseAndReturnedTrue(Integer bookId, Long id);
}

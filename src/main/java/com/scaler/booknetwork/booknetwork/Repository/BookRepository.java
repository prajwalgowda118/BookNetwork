package com.scaler.booknetwork.booknetwork.Repository;

import com.scaler.booknetwork.booknetwork.Models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

   // Page<Book>  findAllByIsShareableAndIsNotArchivedAndId(Boolean shareable, Boolean archived, Pageable pageable,Integer id);

    //Page<Book> findAllByOwner(Pageable pageable,Integer id);

    @Query("""
            
            select book from Book book where book.archived=false and book.shareable=true and book.owner.id=:userID
""")
    Page<Book> findAllDisplayableBooks(Pageable pageable,Long userID);

    //findAllBooksByOwner

   // Page<Book> findAll();
}

package com.scaler.booknetwork.booknetwork.Models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean returned;
    private boolean returnApproved;
    @ManyToOne()
    @JoinColumn(name ="user_id")
    private User user;
    @ManyToOne()
    @JoinColumn(name="book_id")
    private Book book;


    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(updatable = false, nullable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer updatedBy;
}

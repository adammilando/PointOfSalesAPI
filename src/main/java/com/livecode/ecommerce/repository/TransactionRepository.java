package com.livecode.ecommerce.repository;

import com.livecode.ecommerce.model.Entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTimesTampBetween(LocalDate timesTampStart, LocalDate timesTampEnd);
    List<Transaction> findByTimesTamp(LocalDate date);
}

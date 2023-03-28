package com.livecode.ecommerce.repository;

import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SalesDetailRepository extends JpaRepository<SaleDetail, Long> {
    Page<SaleDetail> findByTransactionIn(Collection<Transaction> transactions, Pageable pageable);
}

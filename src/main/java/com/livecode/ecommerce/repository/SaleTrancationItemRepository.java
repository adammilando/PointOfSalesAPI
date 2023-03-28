package com.livecode.ecommerce.repository;

import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleTrancationItemRepository extends JpaRepository<SaleDetail, Long> {
    List<SaleDetail> findByTransactionIn(List<Transaction> transactions);
}

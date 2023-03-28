package com.livecode.ecommerce.service;

import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Request.DailySalesReportRequest;
import com.livecode.ecommerce.model.Request.MontlySalesRepostRequest;
import com.livecode.ecommerce.repository.SaleTrancationItemRepository;
import com.livecode.ecommerce.repository.SaleTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private SaleTransactionRepository transactionRepository;

    private SaleTrancationItemRepository saleTrancationItemRepository;

    @Autowired
    public ReportService(SaleTransactionRepository transactionRepository, SaleTrancationItemRepository saleTrancationItemRepository) {
        this.transactionRepository = transactionRepository;
        this.saleTrancationItemRepository = saleTrancationItemRepository;
    }

    public List<DailySalesReportRequest> getDailySaleReport(LocalDate date) {
        List<Transaction> transactions = transactionRepository.findByTimesTamp(date);
        List<SaleDetail> saleDetails = saleTrancationItemRepository.findByTransactionIn(transactions);

        Map<Long, DailySalesReportRequest> reportMap = new HashMap<>();

        for (SaleDetail saleDetail : saleDetails) {
            Product product = saleDetail.getProduct();
            DailySalesReportRequest report = reportMap.getOrDefault(product.getId(), new DailySalesReportRequest());
            report.setDate(date);
            report.setProductId(product.getId());
            report.setProductName(product.getName());
            report.setQtySold(report.getQtySold() + saleDetail.getQuantity());
            report.setTotalAmount(report.getTotalAmount().add(saleDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(saleDetail.getQuantity()))));
            reportMap.put(product.getId(), report);
        }

        return new ArrayList<>(reportMap.values());
    }

    public List<MontlySalesRepostRequest> getMonthlySaleReport(int month, int year) {
        List<Transaction> transactions = transactionRepository.findByTimesTampBetween(
                LocalDate.of(year, month, 1),
                LocalDate.of(year, month, Month.of(month).length(Year.isLeap(year))));
        List<SaleDetail> saleDetails = saleTrancationItemRepository.findByTransactionIn(transactions);

        Map<Long, MontlySalesRepostRequest> reportMap = new HashMap<>();

        for (SaleDetail saleDetail : saleDetails) {
            Product product = saleDetail.getProduct();
            MontlySalesRepostRequest report = reportMap.getOrDefault(product.getId(), new MontlySalesRepostRequest());
            report.setMonth(month);
            report.setYear(year);
            report.setProductId(product.getId());
            report.setProductName(product.getName());
            report.setQuantitySold(report.getQuantitySold() + saleDetail.getQuantity());
            report.setTotalAmount(report.getTotalAmount().add(saleDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(saleDetail.getQuantity()))));
            reportMap.put(product.getId(), report);
        }
        return new ArrayList<>(reportMap.values());
    }
}

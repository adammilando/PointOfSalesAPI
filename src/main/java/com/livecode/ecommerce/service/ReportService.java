package com.livecode.ecommerce.service;

import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Request.DailySalesReportRequest;
import com.livecode.ecommerce.model.Request.MontlySalesRepostRequest;
import com.livecode.ecommerce.repository.SalesDetailRepository;
import com.livecode.ecommerce.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    private TransactionRepository transactionRepository;

    private SalesDetailRepository salesDetailRepository;

    @Autowired
    public ReportService(TransactionRepository transactionRepository, SalesDetailRepository salesDetailRepository) {
        this.transactionRepository = transactionRepository;
        this.salesDetailRepository = salesDetailRepository;
    }

    public Page<DailySalesReportRequest> getDailySaleReport(LocalDate date,Integer page,Integer size, String direction, String sort) {
        try {
            Sort sortby = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of(page, size, sortby);
            List<Transaction> transactions = transactionRepository.findByTimesTamp(date);
            Page<SaleDetail> saleDetails = salesDetailRepository.findByTransactionIn(transactions,pageable);

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

            List<DailySalesReportRequest> reportList = new ArrayList<>(reportMap.values());
            return new PageImpl<>(reportList, pageable, reportList.size());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public Page<MontlySalesRepostRequest> getMonthlySaleReport(int month, int year, Integer page,Integer size, String direction, String sort) {
        try {
            Sort sortby = Sort.by(Sort.Direction.valueOf(direction), sort);
            Pageable pageable = PageRequest.of(page, size, sortby);
            List<Transaction> transactions = transactionRepository.findByTimesTampBetween(
                    LocalDate.of(year, month, 1),
                    LocalDate.of(year, month, Month.of(month).length(Year.isLeap(year))));
            Page<SaleDetail> saleDetails = salesDetailRepository.findByTransactionIn(transactions, pageable);

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
            List<MontlySalesRepostRequest> reportList = new ArrayList<>(reportMap.values());
            return new PageImpl<>(reportList, pageable, reportList.size());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

package com.livecode.ecommerce.model.Request;

import com.livecode.ecommerce.model.Entities.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DailySalesReportRequest {
    private LocalDate date;

    private Long productId;

    private String productName;

    private int qtySold;

    private BigDecimal totalAmount;

    public DailySalesReportRequest() {
        this.totalAmount = BigDecimal.ZERO;
    }
}

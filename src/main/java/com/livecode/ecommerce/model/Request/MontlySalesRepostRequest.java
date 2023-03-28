package com.livecode.ecommerce.model.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
@Getter
@Setter
public class MontlySalesRepostRequest {

    private int month;
    private int year;
    private Long productId;
    private String productName;
    private int quantitySold;
    private BigDecimal totalAmount;

    public MontlySalesRepostRequest() {
        this.totalAmount = BigDecimal.ZERO;
    }
}

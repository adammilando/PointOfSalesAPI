package com.livecode.ecommerce.model.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private int stock;
    private BigDecimal price;
    private Long category;
}

package com.livecode.ecommerce.model.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private int stock;
    private List<Long> price;
    private Long category;
}

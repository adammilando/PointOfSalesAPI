package com.livecode.ecommerce.model.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesDetailRequest {

    private int quantity;

    private Long product;

    private Long transaction;

}

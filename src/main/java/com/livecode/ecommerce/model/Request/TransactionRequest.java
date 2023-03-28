package com.livecode.ecommerce.model.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {

    private LocalDate timesTamp;

    private Long user;
}

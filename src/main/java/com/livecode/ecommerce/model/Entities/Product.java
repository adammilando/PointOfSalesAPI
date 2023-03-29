package com.livecode.ecommerce.model.Entities;
import com.livecode.ecommerce.model.Entities.Price;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_tb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int stock;
    @OneToMany
    @JoinColumn(name = "price_id")
    private List<Price> price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

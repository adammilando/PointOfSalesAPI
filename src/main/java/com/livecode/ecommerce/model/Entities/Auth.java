package com.livecode.ecommerce.model.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_auth")
public class Auth{
    @Id
    @Column(unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
}

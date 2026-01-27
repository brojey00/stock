package com.example.stock.dao.entities;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @OneToMany
    private Product product;
    private int quantity;
    @OneToOne
    private Entrepot entrepot;
    private int stock_alert;
}

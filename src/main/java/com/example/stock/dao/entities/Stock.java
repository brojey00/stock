package com.example.stock.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Product product;
    private int quantity;
    @ManyToOne(optional = false)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;
    private int stock_alert;
}

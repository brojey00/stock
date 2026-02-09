package com.example.stock.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"buy_price", "sell_price", "Description", "Category","name","unit"})
    private Product product;
    private Integer quantity;
    @ManyToOne(optional = false)
    @JoinColumn(name = "entrepot_id", nullable = false)
    @JsonIgnoreProperties({"location", "city","name"})
    private Entrepot entrepot;
    private Integer stockAlert;
}

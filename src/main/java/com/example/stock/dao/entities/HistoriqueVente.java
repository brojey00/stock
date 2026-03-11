package com.example.stock.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;

    private LocalDate date_vente;
    private Integer quantity_vendue;

    @Enumerated(EnumType.STRING)
    private Day jour_semaine;

    private Integer mois;
    private Integer annee;
}

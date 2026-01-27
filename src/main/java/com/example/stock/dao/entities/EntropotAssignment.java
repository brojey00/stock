package com.example.stock.dao.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EntropotAssignment {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User gestionnaire;

    @OneToOne
    @JoinColumn(name = "entrepot_id", unique = true)
    private Entrepot entrepot;
}

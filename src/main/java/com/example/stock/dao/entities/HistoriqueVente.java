package com.example.stock.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueVente {
    @Id
    private int id;
    @OneToOne
    private Product product;
    @OneToOne
    private Entrepot entrepot;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date date;
    @Enumerated(EnumType.STRING)
    private Day day;

}

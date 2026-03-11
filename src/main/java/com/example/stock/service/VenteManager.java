package com.example.stock.service;

import com.example.stock.dao.entities.*;
import com.example.stock.dao.repositories.EntropotRepository;
import com.example.stock.dao.repositories.HistoriqueVenteRepository;
import com.example.stock.dao.repositories.ProductRepository;
import com.example.stock.dao.repositories.StockRepository;
import com.example.stock.dto.VenteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.example.stock.mapper.VenteMapper.toDtoList;

@Service
public class VenteManager {

    @Autowired
    private HistoriqueVenteRepository historiqueVenteRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntropotRepository entrepotRepository;

    @Transactional
    public HistoriqueVente vendre(User caller, Integer produitId, Integer entrepotId, Integer quantite) {
        Entrepot entrepot;
        if (caller.getRole() == Role.ADMIN) {
            if (entrepotId == null) {
                throw new RuntimeException("Entrepot ID is required for admin sales");
            }
            entrepot = entrepotRepository.findById(entrepotId)
                    .orElseThrow(() -> new RuntimeException("Entrepot not found with ID: " + entrepotId));
        } else if (caller.getRole() == Role.MANAGER) {
            if (caller.getEntrepotAssigne() == null) {
                throw new RuntimeException("Manager is not assigned to any warehouse");
            }
            entrepot = caller.getEntrepotAssigne();
        } else {
            throw new RuntimeException("User does not have permission to perform sales");
        }

        Product product = productRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + produitId));

        Stock stock = stockRepository.findByProductIdAndEntrepotId(product.getId(), entrepot.getId())
                .orElseThrow(() -> new RuntimeException("No stock found for this product in this warehouse"));

        if (stock.getQuantity() == null || stock.getQuantity() < quantite) {
            throw new RuntimeException("Insufficient stock. Available: " + (stock.getQuantity() != null ? stock.getQuantity() : 0));
        }

        stock.setQuantity(stock.getQuantity() - quantite);
        stockRepository.save(stock);

        LocalDate today = LocalDate.now();
        HistoriqueVente historique = new HistoriqueVente();
        historique.setProduct(product);
        historique.setEntrepot(entrepot);
        historique.setDate_vente(today);
        historique.setQuantity_vendue(quantite);
        historique.setJour_semaine(mapDayOfWeek(today.getDayOfWeek()));
        historique.setMois(today.getMonthValue());
        historique.setAnnee(today.getYear());

        return historiqueVenteRepository.save(historique);
    }

    public List<VenteResponse> getHistorique(User caller) {
        if (caller.getRole() == Role.ADMIN) {
            return toDtoList(historiqueVenteRepository.findAll());
        } else if (caller.getRole() == Role.MANAGER) {
            if (caller.getEntrepotAssigne() == null) {
                throw new RuntimeException("Manager is not assigned to any warehouse");
            }
            return toDtoList(historiqueVenteRepository.findByEntrepot(caller.getEntrepotAssigne()));
        }
        throw new RuntimeException("Unauthorized");
    }

    public List<VenteResponse> getHistoriqueByEntrepot(Integer entrepotId) {
        Entrepot entrepot = entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new RuntimeException("Entrepot not found with ID: " + entrepotId));
        return toDtoList(historiqueVenteRepository.findByEntrepot(entrepot));
    }

    private Day mapDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> Day.MONDAY;
            case TUESDAY -> Day.TUESDAY;
            case WEDNESDAY -> Day.WEDNESDAY;
            case THURSDAY -> Day.THURSDAY;
            case FRIDAY -> Day.FRIDAY;
            case SATURDAY -> Day.SATURDAY;
            case SUNDAY -> Day.SUNDAY;
        };
    }
}

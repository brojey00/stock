package com.example.stock.mapper;

import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dto.VenteResponse;

import java.util.List;

public class VenteMapper {
    public static VenteResponse toDto(HistoriqueVente historiqueVente){
        if(historiqueVente == null){return null;}
        return new VenteResponse(historiqueVente.getProduct().getName(),historiqueVente.getEntrepot().getName(), historiqueVente.getQuantity_vendue(),historiqueVente.getJour_semaine(), historiqueVente.getMois(), historiqueVente.getAnnee());

    }
    public static List<VenteResponse> toDtoList(List<HistoriqueVente> historiqueVentes){
        return historiqueVentes == null ? List.of() : historiqueVentes.stream().map(VenteMapper::toDto).toList();
    }
}

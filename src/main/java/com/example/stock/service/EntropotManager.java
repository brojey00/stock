package com.example.stock.service;

import com.example.stock.dao.repositories.EntropotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntropotManager {
    @Autowired
    private EntropotRepository entropotRepository;
}

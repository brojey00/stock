package com.example.stock.service;

import com.example.stock.dao.entities.EntropotAssignment;
import com.example.stock.dao.repositories.EntropotAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntropotAssignmentManager {
    @Autowired
    private EntropotAssignmentRepository entropotAssignmentRepository;
}

package com.example.stock.service;

import com.example.stock.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;

}

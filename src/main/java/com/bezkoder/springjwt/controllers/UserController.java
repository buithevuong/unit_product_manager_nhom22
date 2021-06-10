package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.CustomerRepository;
import com.bezkoder.springjwt.repository.SaleBillRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getCustomer() {
        return userRepository.findAll();
    }
}

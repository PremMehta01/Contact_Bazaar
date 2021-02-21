package com.shivamkibhu.controllers;

import com.shivamkibhu.dao.ContactRepo;
import com.shivamkibhu.dao.UserRepo;
import com.shivamkibhu.entities.Contact;
import com.shivamkibhu.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContactRepo contactRepo;

    // search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){
        User user = userRepo.getUserByUserName(principal.getName());

        List<Contact> contacts = contactRepo.findByNameContainingAndUser(query, user);

        return ResponseEntity.ok(contacts);
    }
}

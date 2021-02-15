package com.shivamkibhu.controllers;

import com.shivamkibhu.dao.UserRepo;
import com.shivamkibhu.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        String email = principal.getName();

        // get the user using email
        User user = userRepo.getUserByUserName(email);
        model.addAttribute("user", user);

        return "normal/user_dashboard";
    }

}

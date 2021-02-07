package com.shivamkibhu.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shivamkibhu.dao.UserRepo;
import com.shivamkibhu.entities.User;
import com.shivamkibhu.helper.Message;

@Controller
public class HomeController {

    @Autowired
    UserRepo userRepo;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Dev branch - Contact Bazaar");
        return "home";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register- Contact Bazaar");
        model.addAttribute("user", new User());
        return "signup";
    }

    // register user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model, HttpSession session) {

        try {
            if (!agreement) {
                System.out.println("You have not checked agreement box");
                throw new Exception("You have not checked agreement box");
            }

            if (result.hasErrors()) {
                System.out.println("Error: " + result.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);

            System.out.println("Agrement " + agreement);
            System.out.println("User " + user);

            userRepo.save(user);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully registered!", "alert-success"));
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Some went wrong- " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

}

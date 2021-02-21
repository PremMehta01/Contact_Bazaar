package com.shivamkibhu.controllers;

import com.shivamkibhu.dao.ContactRepo;
import com.shivamkibhu.dao.UserRepo;
import com.shivamkibhu.entities.Contact;
import com.shivamkibhu.entities.User;
import com.shivamkibhu.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContactRepo contactRepo;


    // This executes everytime whenever we call any api in this class.
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String email = principal.getName();

        User user = userRepo.getUserByUserName(email);
        model.addAttribute("user", user);
    }


    // dashboard
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "Dashboard");
        return "normal/user_dashboard";
    }


    // Add contacts from dashboard
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }


    // Save contact
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, HttpSession session) {

        try {
            // image handler
            if (file.isEmpty()) {
                contact.setImage("default.png");
            } else {
                String userImageName = file.getOriginalFilename();
                contact.setImage(userImageName);

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + userImageName);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }


            String email = principal.getName();
            User user = userRepo.getUserByUserName(email);

            user.getContacts().add(contact);  // adding contact to user
            contact.setUser(user);  // adding user to contact

            userRepo.save(user);

            // add required details to session, so that we can show success toast message on the top
            session.setAttribute("message", new Message("Your contact has been successfully added. Please add more!!", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error, while saving contact: " + e.getMessage());

            // add required details to session, so that we can show error/danger toast message on the top
            session.setAttribute("message", new Message("Something went wrong. Please try again!!", "danger"));
        }

        return "normal/add_contact_form";
    }


    // show all contacts
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer pageIdx, Model model, Principal principal) {
        model.addAttribute("title", "User Contacts");

        String email = principal.getName();
        User user = userRepo.getUserByUserName(email);

        int itemPerPage = 10;   // Number of items per page
        Pageable pageable = PageRequest.of(pageIdx, itemPerPage);
        Page<Contact> contacts = contactRepo.findContactsByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", pageIdx);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }


    // show single contact details
    @GetMapping("/{id}/contact")
    public String showContactDetail(@PathVariable("id") Integer id, Model model, Principal principal) {

        Optional<Contact> contactOptional = contactRepo.findById(id);
        Contact contact = contactOptional.get();

        String email = principal.getName();
        User user = userRepo.getUserByUserName(email);

        // check authorization if this user has permission to see this contact or not
        if (user.getId() == contact.getUser().getId())     // so that any user cannot manually edit the url and get the contact details which is under another user
            model.addAttribute("contact", contact);

        return "normal/contact_detail";
    }


    // delete contact
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Integer id, Principal principal, HttpSession session){
        Optional<Contact> optionalContact = contactRepo.findById(id);
        Contact contact = optionalContact.get();

        String email = principal.getName();
        User user = userRepo.getUserByUserName(email);

        // check authorization(if this user has permission to delete this contact)
        if(contact.getUser().getId() == user.getId()) {
            // TODO: delete contact image from /static/img/{contact.getImage()}

            contactRepo.delete(contact);  // contactRepo.deleteById(id);
            session.setAttribute("message", new Message("Contact deleted successfully", "success"));
        }else{
            session.setAttribute("message", new Message("You are not authorized to delete this contact", "danger"));
        }

        return "redirect:/user/show-contacts/0";
    }

}

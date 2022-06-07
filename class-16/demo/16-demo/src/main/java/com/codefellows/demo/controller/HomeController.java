package com.codefellows.demo.controller;

import com.codefellows.demo.model.AppUser;
import com.codefellows.demo.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    // bring in the repo
    @Autowired
    AppRepository appRepository;

    // Step 8: make a home page
    @GetMapping("/")
    public String getHomePage(Principal p, Model m){

        if (p != null){
            String username = p.getName();
            AppUser appUser = appRepository.findByUsername(username);

            m.addAttribute("username", username);
        }

        return "index";
    }
}

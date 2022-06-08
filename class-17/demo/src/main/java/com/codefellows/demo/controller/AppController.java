package com.codefellows.demo.controller;

import com.codefellows.demo.model.AppUser;
import com.codefellows.demo.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

// Step 2: Make a controller
@Controller
public class AppController {

    @Autowired
    AppRepository appRepository;

    // Autowire our passwordEncoder THIS IS A BEAN!!!
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    // Step 7: Make a login page (which Spring Security will POST to magically!)
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    // Step 6A: Make a signup user page and controller method
    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    // Step 6B: Create user when submitting
    @PostMapping("/signup")
    public RedirectView createUser(String username, String password) {
        // hash the password!!!
        String hashedPw = passwordEncoder.encode(password);
        AppUser newUser = new AppUser(username, hashedPw);
        appRepository.save(newUser); // try/catch with dupe user exception???

        authWithHttpServletRequest(username, password);

        return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username, String password)
    {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("Error while logging in.");
            e.printStackTrace();
        }
    }
}

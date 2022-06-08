package com.codefellows.demo.controller;

import com.codefellows.demo.model.AppUser;
import com.codefellows.demo.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.lang.module.ResolutionException;
import java.security.Principal;
import java.time.LocalDateTime;

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

        // throw a 404 error,
//        throw new ResourceNotFoundException("This is a 404");

        return "index";
    }

    // CUSTOM 404 exception!!
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{
        ResourceNotFoundException(String message) {super(message); }
    }


    @GetMapping("/test")
    // What is Principal? -> It is the Authenticated User.
    public String getTestPage(Principal p, Model m){

        if (p != null){ // not strictly required if your WebSecuityConfig is correct
            String username = p.getName();
            AppUser appUser = appRepository.findByUsername(username);

            m.addAttribute("username", username);
        }
        return "/test";
    }


    // /user/
    @GetMapping("/user/{id}")
    public String getUserInfo(Principal p, Model m, @PathVariable Long id){
        // This is session info
        if (p != null){ // not strictly required if your WebSecuityConfig is correct
            String username = p.getName();
            AppUser appUser = appRepository.findByUsername(username);

            m.addAttribute("username", username); // authenticated username
        }
        // DB call to get user info
        AppUser appUser = appRepository.findById(id).orElseThrow();
        m.addAttribute("appUsername", appUser.getUsername()); // database username
        m.addAttribute("appUserId", appUser.getId());

        m.addAttribute("testDate", LocalDateTime.now());

        return "/user-info";
    }

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String username, String nickname)
    {
        // given username and sessions username match
        if ((p != null) && (p.getName().equals(username)))
        {
            AppUser dinoUser = appRepository.findById(id).orElseThrow();
            dinoUser.setUsername(username);
            appRepository.save(dinoUser);
        }

        return new RedirectView("/users/" + id);
    }

}

package com.codefellows.auth;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Controller
public class AuthController {

    // wire up our repo
    @Autowired
    SiteUserRpository siteUserRpository;

    @GetMapping("/")
    public String getHomepage()
    {
        return "index";
    }

    // get route to /login
    @GetMapping("/login")
    public String getLoginPage() {return "login"; }

    // post route /login
    @PostMapping("/login")
    public RedirectView logInUser(String username, String _password){
        // compare given crdentials to the credentials in the database
        // plaintext password
        SiteUser userFromDB = siteUserRpository.findByUsername(username);

        if((userFromDB == null) || (BCrypt.checkpw(_password, userFromDB.password))){
            return new RedirectView("/login");
        }

        return new RedirectView("/");
    }


    // get route to signup
    @GetMapping("/signup")
    public String getSignupPage()
    {
        return "signup";
    }

    //post route to signup
    @PostMapping("/signup")
    public RedirectView signUpUser(Model m, String username, String password){
        // bcrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        SiteUser newSiteUser = new SiteUser(username, hashedPassword);
        siteUserRpository.save(newSiteUser);

        return new RedirectView("login");

    }
}

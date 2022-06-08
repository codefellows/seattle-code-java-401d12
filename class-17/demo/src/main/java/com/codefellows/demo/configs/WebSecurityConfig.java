package com.codefellows.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Step 5: Make WebSecurityConfig extends WebSecurityConfigurerAdapter
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // Step 5a: wire up UserDetailsService
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    // Step 5b:Wire up a reusable passwordEncoder
    // password encoder BEAN!!
    // We must turn it into a BEAN
    // BEANS follow D.R.Y.
    @Bean
    public PasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    // Step 5C: Link the userDetailsService and the passwordEncoder via a configure() method
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // Step 5D: Configure the security of our application via HttpSecurity
    // This is where we will do the work of how users can access the site
    @Override
    // reference: https://stackoverflow.com/questions/62118216/spring-security-what-do-authorizerequests-anyrequest-and-authenticated-d
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable() // CSRF: submitting requests blindly to the server as you (meaning using your cookies)
                .authorizeRequests()
                .antMatchers("/").permitAll() // allow all users to have access to this
                .antMatchers("/user", "/login", "/signup").permitAll()
                .anyRequest().authenticated() // will restrict the access for any other endpoint other than PUBLIC_URL, and the user must be authenticated.
                .and()
                .formLogin()
                .defaultSuccessUrl("/")
                .loginPage("/login") // any unauthorized request goes here
                .and()
                .logout()
                .logoutSuccessUrl("/login"); // make sure your bttn for logout is pointing to this path

    }
}

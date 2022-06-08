package com.codefellows.demo.configs;

import com.codefellows.demo.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// Step 3: Make a UserDetailsServiceImpl that implements UserDetailsService
@Service // Spring instantiates @components(service, entity, controller) AS BEANS at runtime
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AppRepository appRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appRepository.findByUsername(username);
    }
}

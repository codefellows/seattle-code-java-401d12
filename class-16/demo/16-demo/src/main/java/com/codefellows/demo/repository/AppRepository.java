package com.codefellows.demo.repository;

import com.codefellows.demo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

// Step 1B: Make user repository
public interface AppRepository extends JpaRepository<AppUser, Long> {

    // custom queries
    AppUser findByUsername(String username);

}

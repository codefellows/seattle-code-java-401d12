package com.codefellows.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRpository extends JpaRepository<SiteUser, Long> {
    SiteUser findByUsername(String username);
}

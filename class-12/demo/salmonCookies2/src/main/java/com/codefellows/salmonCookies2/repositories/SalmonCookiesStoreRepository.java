package com.codefellows.salmonCookies2.repositories;

import com.codefellows.salmonCookies2.models.SalmonCookiesStore;
import org.springframework.data.jpa.repository.JpaRepository;

// Make a repo for the data value. WE DON"T IMPLEMENT THIS. This is a service.
// this is available to your whole app through @Autowired
public interface SalmonCookiesStoreRepository extends JpaRepository<SalmonCookiesStore, Long> {

  // The reason we are using an interface, is so we can create CUSTOM CRUD queries

  // DARK MAGIC that we made happen with a specific function name
  public SalmonCookiesStore findByName(String name);
}

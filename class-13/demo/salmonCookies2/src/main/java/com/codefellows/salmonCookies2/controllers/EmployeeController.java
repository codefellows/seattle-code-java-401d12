package com.codefellows.salmonCookies2.controllers;

import com.codefellows.salmonCookies2.models.Employee;
import com.codefellows.salmonCookies2.models.SalmonCookiesStore;
import com.codefellows.salmonCookies2.repositories.EmployeeRepository;
import com.codefellows.salmonCookies2.repositories.SalmonCookiesStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class EmployeeController {

    @Autowired
    SalmonCookiesStoreRepository salmonCookiesStoreRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping("/add-employee")
    public RedirectView addEmployeeToStore(String name, Boolean dayshift, String store){
        SalmonCookiesStore salmonCookieStore = salmonCookiesStoreRepository.findByName(store);
        Employee newEmployee = new Employee(name, dayshift, salmonCookieStore);
        employeeRepository.save(newEmployee);

        return new RedirectView("/");
    }
}

package com.codefellows.salmonCookies2.repositories;

import com.codefellows.salmonCookies2.models.Employee;
import com.codefellows.salmonCookies2.models.SalmonCookiesStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // can we leave this blank? Yes
    //This is for custom queries
}

package com.codefellows.salmonCookies2.models;

import javax.persistence.*;
import java.util.List;


// Entities get stored in the DB!!
// 1. add @Entity to our class
@Entity
public class SalmonCookiesStore
{
  //2. Add @Id and @GeneratedValie annotations
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //IF you need longer than 255 characters, use these 2 annotations
    // @Lob
    // @Type(type = "org.hibernate.type.TextType")

    String name;
    Integer averageCookiesPerDay;

    // ONeToMany relationship with employee
    // call this salmonCookiesStore
    // mapBy store
    @OneToMany(mappedBy = "salmonCookiesStore")
    List<Employee> employeesAtThisStore;


    // Relationship - one to many - employees
    protected SalmonCookiesStore()
    {

    }

    public SalmonCookiesStore(String name, Integer averageCookiesPerDay)
    {
        this.name = name;
        this.averageCookiesPerDay = averageCookiesPerDay;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getAverageCookiesPerDay()
    {
        return averageCookiesPerDay;
    }

    public void setAverageCookiesPerDay(Integer averageCookiesPerDay)
    {
        this.averageCookiesPerDay = averageCookiesPerDay;
    }

    public List<Employee> getEmployeesAtThisStore() {
        return employeesAtThisStore;
    }

    public void setEmployeesAtThisStore(List<Employee> _employeesAtThisStore) {
        this.employeesAtThisStore = _employeesAtThisStore;
    }
}

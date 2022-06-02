package com.codefellows.salmonCookies2.models;


import javax.persistence.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    Boolean dayshift;

    // mapToStore
    @ManyToOne
    SalmonCookiesStore salmonCookiesStore;
    // ManyToOne relationship salmonCookieStore


    public Employee() {
    }

    public Employee(String name, Boolean dayshift, SalmonCookiesStore store) {
        this.name = name;
        this.dayshift = dayshift;
        salmonCookiesStore = store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDayshift() {
        return dayshift;
    }

    public void setDayshift(Boolean dayshift) {
        this.dayshift = dayshift;
    }
}

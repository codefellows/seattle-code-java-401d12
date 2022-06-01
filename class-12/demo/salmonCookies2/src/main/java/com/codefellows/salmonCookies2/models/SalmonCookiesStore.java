package com.codefellows.salmonCookies2.models;

import javax.persistence.*;


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
}

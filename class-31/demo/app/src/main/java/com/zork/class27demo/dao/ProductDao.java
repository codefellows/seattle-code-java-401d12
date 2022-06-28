package com.zork.class27demo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.zork.class27demo.models.Product;

import java.util.List;

@Dao // Think of this like a Spring JPA repository, but we have to implement more stuff ourselves
public interface ProductDao {
    // CREATE
    @Insert
    public void insertAProduct(Product product);

    // find all products
    @Query("SELECT * FROM Product")
    public List<Product> findAll();

    @Query("SELECT * FROM Product ORDER BY name ASC")
    public List<Product> findAllSortedByName();

    @Query("SELECT * FROM Product WHERE id = :id")
    Product findByAnId(Long id);

}

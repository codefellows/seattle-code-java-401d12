package com.zork.class27demo.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

// TODO: Step 2-1: Make a data class
@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    String name;
    String description;
    java.util.Date dateCreated;
    ProductCategoryEnum productCategory;


    public Product(String name, String description, Date dateCreated, ProductCategoryEnum productCategory) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.productCategory = productCategory;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ProductCategoryEnum getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryEnum productCategory) {
        this.productCategory = productCategory;
    }

    public enum ProductCategoryEnum {
        CLOTHES("Clothes"),
        Electronics("Electronics"),
        PERISHABLE_GOODS("Perishable Goods"),
        OFFICE_SUPPLIES("Office Supplies"),
        MISC("Miscellaneous");

        private final String productText;

        ProductCategoryEnum(String _productText) {
            this.productText = _productText;
        }

        public String getProductText() {
            return productText;
        }

        public static ProductCategoryEnum fromString(String possibleProductText) {
            for (ProductCategoryEnum product : ProductCategoryEnum.values())
            {
                if (product.productText.equals(possibleProductText))
                {
                    return product;
                }
            }

            return null;
        }


        @NonNull
        @Override
        public String toString() {
            if (productText == null) {
                return "";
            }
            return productText;
        }
    }
}

package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.zork.class27demo.R;
import com.zork.class27demo.models.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setUpSpinner();
        setUpSaveButton();

    }


    private void setUpSpinner(){
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        productCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Product.ProductCategoryEnum.values()));
    }

    private void setUpSaveButton(){
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        Button saveNewProductButton = findViewById(R.id.addAProductSaveButton);
        saveNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = ((EditText) findViewById(R.id.addAProductNameInput)).getText().toString();
                String productDescription = ((EditText) findViewById(R.id.addAProductDescriptionInput)).getText().toString();
                java.util.Date newDate = new Date();
                Product.ProductCategoryEnum productCategory = Product.ProductCategoryEnum.fromString(productCategorySpinner.getSelectedItem().toString());

                Product newProduct = new Product(productName, productDescription, newDate, productCategory);

//                database
//                Intent goToHomeActivity = new Intent(AddProductActivity.this, HomeActivity.class);
//                startActivity(goToHomeActivity);

            }
        });
    }
}

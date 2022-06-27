package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.zork.class27demo.R;
import com.zork.class27demo.database.TaskMasterDatabase;
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

    TaskMasterDatabase taskMasterDatabase;
    public static final String DATABASE_NAME = "zork_task_master"; // needs to be the same everywhere in our app!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        taskMasterDatabase = Room.databaseBuilder(
                getApplicationContext(), //  so that we only have a single database across the whole app
                TaskMasterDatabase.class,
                DATABASE_NAME)
                .allowMainThreadQueries()  // Don't do this in a real app!
                .fallbackToDestructiveMigration() // If Room gets confused, it tosses your database; don't use this in production!
                .build();

        setUpSpinner();
        setUpSaveButton(taskMasterDatabase);

    }


    private void setUpSpinner(){
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        productCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Product.ProductCategoryEnum.values()));
    }

    private void setUpSaveButton(TaskMasterDatabase database){
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

                database.productDao().insertAProduct(newProduct);
                Intent goToHomeActivity = new Intent(AddProductActivity.this, HomeActivity.class);
                startActivity(goToHomeActivity);

            }
        });
    }
}

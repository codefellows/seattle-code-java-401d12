package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Product;
import com.zork.class27demo.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    public static final String TAG = "AddProductActivity";
    String[] categories = {"Clothes", "Electronics", "Perishable_Goods", "Office_Supplies", "Misc"};
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
                // TODO: get rid of enum
                categories));
    }

    private void setUpSaveButton(){
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        Button saveNewProductButton = findViewById(R.id.addAProductSaveButton);
        saveNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = ((EditText) findViewById(R.id.addAProductNameInput)).getText().toString();
                String productDescription = ((EditText) findViewById(R.id.addAProductDescriptionInput)).getText().toString();
                // AWS time util
                String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

                // TODO: LOOK AT THE MODEL BUILDER
                Product newProduct = Product.builder()
                        .name(productName)
                        .description(productDescription)
                        .dateCreated(new Temporal.DateTime(currentDateString))
                        // TODO: get rid of enum
                        .productCategory(productCategorySpinner.getSelectedItem().toString())
                        .build();
                // TODO: THIS IS HOW WE CRUD DynamoDB
                Amplify.API.mutate(
                        ModelMutation.create(newProduct), // making a Graphql request to the cloud
                        successResponse -> Log.i(TAG, "AddProductActivity.onCreate(): made a product successfully"),  // success callback
                        failureResponse -> Log.i(TAG, "AddProductActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
                );
            }
        });
    }
}

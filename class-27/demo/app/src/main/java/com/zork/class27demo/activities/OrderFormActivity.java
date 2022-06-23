package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zork.class27demo.R;

public class OrderFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        setUpProductName();
    }



    private void setUpProductName(){
        Intent callingIntent = getIntent();
        String productNameString = null;
        if(callingIntent != null){
            productNameString = callingIntent.getStringExtra(HomeActivity.PRODUCT_NAME_EXTRA_TAG);
        }

        TextView orderFormProductName = findViewById(R.id.orderFormProductName);
        if(orderFormProductName != null){
            orderFormProductName.setText(productNameString);
        } else {
            orderFormProductName.setText(R.string.no_product_name);

        }
    }

}
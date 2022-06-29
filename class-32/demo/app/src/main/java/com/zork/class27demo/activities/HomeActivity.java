package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zork.class27demo.R;
import com.zork.class27demo.adapter.ProductListRecyclerViewAdapter;
import com.zork.class27demo.models.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String PRODUCT_NAME_EXTRA_TAG = "productName";
    SharedPreferences preferences;

    ProductListRecyclerViewAdapter adapter;
    List<Product> products = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize shared pref
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        products = new ArrayList<>();
        products.add(new Product("Test Product", "The test product description", new java.util.Date(), Product.ProductCategoryEnum.CLOTHES));

//        products = taskMasterDatabase.productDao().findAll();

        setUpSettingsImageView();
        setUpOrderButton();
        setUpProductListRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get my nickname!
        String userNickname = preferences.getString(UserSettingsActivity.USER_NICKNAME_TAG, "No Nickname");
        // set my nickname to the view
        TextView userNicknameText = findViewById(R.id.homeNickname);
        userNicknameText.setText(userNickname);
    }

    private void setUpSettingsImageView(){
        ImageView userSettingsImageView = findViewById(R.id.userSettingsLink);
        userSettingsImageView.setOnClickListener(v -> {
            Intent goToUserSettingsIntent = new Intent(HomeActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettingsIntent);
        });
    }

    private void setUpOrderButton(){
        // Grab the order button
        Button orderButton = findViewById(R.id.homeOrderButton);
        // setup onClick listener
        orderButton.setOnClickListener(v -> {
        // Setup intent to go to OrderFOrmActivity
            Intent goToAddAProductActivity = new Intent(HomeActivity.this, AddProductActivity.class);
        // start activity
            startActivity(goToAddAProductActivity);
        });
    }

    private void setUpProductListRecyclerView(){
        RecyclerView productListRecyclerView = findViewById(R.id.productListRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        productListRecyclerView.setLayoutManager(layoutManager);


        adapter = new ProductListRecyclerViewAdapter(products, this);


        productListRecyclerView.setAdapter(adapter);

    }
}
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize shared pref
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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
        ImageView userSettingsImageView = (ImageView) findViewById(R.id.userSettingsLink);
        userSettingsImageView.setOnClickListener(v -> {
            Intent goToUserSettingsIntent = new Intent(HomeActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettingsIntent);
        });
    }

    private void setUpOrderButton(){
        // Grab the order button
        Button orderButton = findViewById(R.id.homeOrderButton);
        // setuyp onClick listener
        orderButton.setOnClickListener(v -> {
        // Setup intent to go to ORderFOrmActivity
            Intent goToOrderFormActivity = new Intent(HomeActivity.this, OrderFormActivity.class);
        // Include an Extra with the event
            goToOrderFormActivity.putExtra(PRODUCT_NAME_EXTRA_TAG, "Zorkypoo");
        // start activity
            startActivity(goToOrderFormActivity);
        });
    }

    // TODO: Step 1-1: Add a RecyclerView to the Activity in the WSYIWYG editor
    // TODO: Step 1-2: Grab the RecyclerView
    private void setUpProductListRecyclerView(){
        RecyclerView productListRecyclerView = findViewById(R.id.productListRecyclerView);
        // TODO: Step 1-3: Set the layout manager of the RecyclerView to a LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        // TODO: Step1-3: set the layout manager
        productListRecyclerView.setLayoutManager(layoutManager);

        // TODO: Step 2-2: Make some data items
        List<Product> products = new ArrayList<>();

        products.add(new Product("Pens"));
        products.add(new Product("Pencils"));
        products.add(new Product("Binders"));
        products.add(new Product("Paperclips"));
        products.add(new Product("Paper"));
        products.add(new Product("Monitors"));
        products.add(new Product("Flash Drives"));

        // TODO: Step 1-5: Create and attach the RecyclerView.Adapter
        // TODO: Step 2-3: Hand in data items(products array)
        // TODO: Step: 3-2 Give context to our adapter
        ProductListRecyclerViewAdapter adapter = new ProductListRecyclerViewAdapter(products, this);
        // TODO Step: 1-5: set the adapter recyclerview
        productListRecyclerView.setAdapter(adapter);

    }
}
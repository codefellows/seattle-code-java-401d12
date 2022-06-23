package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zork.class27demo.R;

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
}
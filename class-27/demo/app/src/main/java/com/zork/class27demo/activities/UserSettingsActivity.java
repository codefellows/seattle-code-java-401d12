package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.zork.class27demo.R;

public class UserSettingsActivity extends AppCompatActivity {
    // setup shared preferences
    SharedPreferences preferences;
    // prference tag
    public static final String USER_NICKNAME_TAG = "userNickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // i create the shared Preference instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userNickname = preferences.getString(USER_NICKNAME_TAG, ""); // if there is no shared pref, will display 2nd string
        if(!userNickname.isEmpty()){
            EditText userNicknameEdited = findViewById(R.id.userNameInput);
            userNicknameEdited.setText(userNickname);
        }

        // get saved button
        Button userSaveButton = findViewById(R.id.userSettingsSaveButton);
        userSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setting up the editor.
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                // grab the edit text for nickname
                EditText userNicknameText = findViewById(R.id.userNameInput);
                String userNicknameString = userNicknameText.getText().toString();
                
                preferenceEditor.putString(USER_NICKNAME_TAG, userNicknameString);
                preferenceEditor.apply(); // Nothing saves unless you do this, DONT FORGET!!

                Toast.makeText(UserSettingsActivity.this, "Settins saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
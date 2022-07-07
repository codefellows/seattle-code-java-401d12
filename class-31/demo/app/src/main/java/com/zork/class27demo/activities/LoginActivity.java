package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.zork.class27demo.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent callingIntent = getIntent();
        String email = callingIntent.getStringExtra(VerifyAccountActivity.VERIFY_ACCOUNT_EMAIL_TAG);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        usernameEditText.setText(email);

        Button loginButton = (Button) findViewById(R.id.loginHomeButton);
        loginButton.setOnClickListener(v ->
        {
            String username = usernameEditText.getText().toString();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

            Amplify.Auth.signIn(username,
                    password,
                    success ->
                    {
                        Log.i(TAG, "Login succeeded: " + success.toString());
                        Intent goToProductListIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(goToProductListIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Login failed: " + failure.toString());
                        runOnUiThread(() ->
                                {
                                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                }
                        );
                    }
            );
        });

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v ->
        {
            Intent goToSignUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(goToSignUpIntent);
        });
    }
}
package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.zork.class27demo.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";
    public static final String SIGNUP_EMAIL_TAG = "Signup_Email_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signupSubmitButton = (Button) findViewById(R.id.signupSubmitButton);
        signupSubmitButton.setOnClickListener(v ->
        {
            String username = ((EditText) findViewById(R.id.signupUsernameEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.signupPasswordEditText)).getText().toString();

            Amplify.Auth.signUp(username,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .userAttribute(AuthUserAttributeKey.nickname(), "Alex")
                            .build(),
                    success ->
                    {
                        Log.i(TAG, "Signup succeeded: " + success.toString());
                        Intent goToLogInIntent = new Intent(SignupActivity.this, VerifyAccountActivity.class);
                        goToLogInIntent.putExtra(SIGNUP_EMAIL_TAG, username);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Signup failed with username: " + "ed@codefellows.com" + " with this message: " + failure.toString());
                        runOnUiThread(() ->
                                {
                                    Toast.makeText(SignupActivity.this, "Signup failed!", Toast.LENGTH_SHORT).show();
                                }
                        );
                    }
            );
        });


    }
}
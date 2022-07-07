package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.zork.class27demo.R;
public class VerifyAccountActivity extends AppCompatActivity {

    public static final String TAG = "VerifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "Verify_Account_Email_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        Intent callingIntent = getIntent();
        String email = callingIntent.getStringExtra(SignupActivity.SIGNUP_EMAIL_TAG);
        EditText usernameEditText = (EditText) findViewById(R.id.verifyAccountUsernameEditText);
        usernameEditText.setText(email);

        Button verifyAccountVerifyButton = (Button) findViewById(R.id.verifyAccountVerifyButton);
        verifyAccountVerifyButton.setOnClickListener(v ->
        {
            String username = usernameEditText.getText().toString();
            String verificationCode = ((EditText) findViewById(R.id.verifyAccountVerificationCodeEditText)).getText().toString();

            Amplify.Auth.confirmSignUp(username,
                    verificationCode,
                    success ->
                    {
                        Log.i(TAG, "Verification succeeded: " + success.toString());
                        Intent goToLogInIntent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                        goToLogInIntent.putExtra(VERIFY_ACCOUNT_EMAIL_TAG, username);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Verification failed with username: " + username + " with this message: " + failure.toString());
                        runOnUiThread(() ->
                                {
                                    Toast.makeText(VerifyAccountActivity.this, "Verify account failed!", Toast.LENGTH_SHORT).show();
                                }
                        );
                    }
            );
        });

    }
}
package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Product;
import com.zork.class27demo.R;
import com.zork.class27demo.adapter.ProductListRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "homeactivity";
    public static final String PRODUCT_ID_TAG = "Product ID Tag";
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

        setUpSettingsImageView();
        setUpOrderButton();
        setUpProductListRecyclerView();
        setUpLoginOutButtons();
//        manualFileUploader();
    }

    public void manualFileUploader(){
        // Manually create an S3 file for testing

        String testFilename = "testFileName";
        File testFile = new File(getApplicationContext().getFilesDir(), testFilename);

        try
        {
            BufferedWriter testFileBufferedWriter = new BufferedWriter(new FileWriter(testFile));
            testFileBufferedWriter.append("Some test text here\nAnother line of test text");
            testFileBufferedWriter.close();  // Make sure to do this or the text may not be saved!
        } catch (IOException ioe)
        {
            Log.e(TAG, "Could not write file locally with filename: " + testFilename);
        }

        // S3 is EXPECTING a key, this is how you reference your files
        String testFileS3Key = "someFileOnS3.txt";

        Amplify.Storage.uploadFile(
                testFileS3Key,
                testFile,
                success ->Log.i(TAG, "S3 upload succeeded! Key is: " + success.getKey()),
                failure -> Log.i(TAG, "S3 upload failed! " + failure.getMessage())
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserDetails();

//        // get my nickname!
//        String userNickname = preferences.getString(UserSettingsActivity.USER_NICKNAME_TAG, "No Nickname");
//        // set my nickname to the view
//        TextView userNicknameText = findViewById(R.id.homeNickname);
//        userNicknameText.setText(userNickname);

        // TODO: HOW WE READ FORM amplify/dynamo
        Amplify.API.query(
                ModelQuery.list(Product.class),
                success ->
                {
                    // TODO: setup error logger on success.getErrors
                    Log.i(TAG, "Read products successfully!");
                    products.clear();

                    for (Product databaseProduct : success.getData())
                    {
//                        String contactName = "zork"; // this could be the sharedPref name
//                        if(databaseProduct.getContactPerson().getFullName().equals(contactName)){
                            products.add(databaseProduct);
//                        }
                    }

                    runOnUiThread(() ->
                    {
                        //adapter.products = products;
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read products successfully!")
        );
    }

    public void fetchUserDetails(){
        String nickname = "";
        Amplify.Auth.fetchUserAttributes(
                success -> {
                    Log.i(TAG, "Fetch user atts success! " + success.toString());
                    for (AuthUserAttribute authUserAttribute : success) {
                        if(authUserAttribute.getKey().getKeyString().equals("nickname")){
                            String userNickname = authUserAttribute.getValue();
                            runOnUiThread(() -> {
                                ((TextView) findViewById(R.id.homeNickname)).setText(userNickname);
                            });
                        }
                    }
                },
                failure -> {
                    Log.i(TAG, "Fetch user atts failed: " + failure.toString());
                }
        );
    }

    public void handleSignIn(){
        Amplify.Auth.signIn("alex.white@codefellows.com",
                "p@ssw0rd",
                success ->
                {
                    Log.i(TAG, "Login succeeded: " + success.toString());
                },
                failure ->
                {
                    Log.i(TAG, "Login failed: " + failure.toString());
                }
        );
    }

    // Conditional Rendering! FTW
    public void setUpLoginOutButtons(){
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Button loginButton = findViewById(R.id.loginHomeButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        if(authUser == null){
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
        } else {
            loginButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
        }
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
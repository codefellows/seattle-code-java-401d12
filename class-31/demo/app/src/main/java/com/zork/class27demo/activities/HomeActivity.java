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

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Contact;
import com.amplifyframework.datastore.generated.model.Product;
import com.zork.class27demo.R;
import com.zork.class27demo.adapter.ProductListRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "homeactivity";
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

        // Hardcoding Contacts (like your lab asks you to do)
//        Contact contact1 = Contact.builder()
//                .email("Zork@home.com")
//                .fullName("Rizorkopasso")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(contact1),
//                successResponse -> Log.i(TAG, "HomeActivity.onCreate(): made a contact successfully"),  // success callback
//                failureResponse -> Log.i(TAG, "HomeActivity.onCreate(): contact failed with this response: " + failureResponse)  // failure callback
//        );


        // HARDCODE COGNITO SIGNUP
//        Amplify.Auth.signUp("alex.white@codefellows.com", // use email address as username in all Cognito calls
//                "p@ssw0rd",  // Cognito's default password policy is 8 characters, no other requirements
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.email(), "alex.white@codefellows.com")
//                        .userAttribute(AuthUserAttributeKey.nickname(), "ALEX")
//                        .build(),
//                success -> {
//            Log.i(TAG, "Signup succeeded " + success.toString());
//                },
//                failure -> {
//            Log.i(TAG, "Signup failed with message: " + failure.toString());
//                }
//                );

        // HARDCODE confirm verification code
//        Amplify.Auth.confirmSignUp("alex.white@codefellows.com",
//                "085390",
//                success ->{Log.i(TAG, "Verification succeeded: " + success.toString());},
//                failure ->{Log.i(TAG, "Verification failed: " + failure.toString());}
//                );

        //Hardcoded signin
//        handleSignIn();

        Amplify.Auth.signOut(
                () ->
                {
                    Log.i(TAG, "Logout succeeded!");
                },
                failure ->
                {
                    Log.i(TAG, "Logout failed: " + failure.toString());
                }
        );




        setUpSettingsImageView();
        setUpOrderButton();
        setUpProductListRecyclerView();
        setUpLoginOutButtons();
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
        Button loginButton = findViewById(R.id.loginButton);
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
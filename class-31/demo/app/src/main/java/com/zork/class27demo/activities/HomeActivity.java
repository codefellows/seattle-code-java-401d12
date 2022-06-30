package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Contact;
import com.amplifyframework.datastore.generated.model.Product;
import com.zork.class27demo.R;
import com.zork.class27demo.adapter.ProductListRecyclerViewAdapter;

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
    // Steps for adding Amplify to your app
    // 1. Remove Room from your app
    //   1A. Delete the Gradle Room dependencies in app's (lower-level) build.gradle
    //   1B. Delete database class
    //   1C. Delete DAO class
    //   1D. Remove `@Entity` and `@PrimaryKey` annotations from the Product model class
    //   1E: Delete the database variables and instantiation from each Activity that uses them
    //   1F: Comment out DAO usages in each Activity that uses them
    // 3. Run `amplify configure`
    // 4. Add Amplify Gradle dependencies in build.gradle files
    // 5. Run `amplify init`
    // 6. Run `amplify add api` (or `amplify update api`)
    // 7. Run `amplify push`
    // 8. Change model in "amplify/backend/api/amplifyDatasource/schema.graphql" to match your app's model
    // 9. Run `amplify api update` -> Disable conflict resolution
    // 10. Run `amplify push --allow-destructive-graphql-schema-updates` DID NOT WORK FOR ME
    // 11. Run `amplify codegen models`
    // 12A. Add an application class that extends Application and configures Amplify
    // 12B. Put the application class name in your AndroidManifest.xml
    // 12C. Uninstall the app on your emulator
    // 13. Convert every usage of model classes to use Amplify generated models in app/src/main/java/com/amplifyframework/datastore/generated/model
    //   13A. Instantiate classes using builder
    //   13B. Get data elements via getters (if you aren't already)
    // 14. Convert all DAO usages to Amplify.API calls
    // 15. Update RecyclerView adapter's collection via runOnUiThread()
    // 16. Fix date output in RecyclerView items
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize shared pref
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        products = new ArrayList<>();

//        // Hardcoding Contacts (like your lab asks you to do)
//        Contact contact1 = Contact.builder()
//                .email("Zork@home.com")
//                .fullName("Rizorkopasso")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(contact1),
//                successResponse -> Log.i(TAG, "HomeActivity.onCreate(): made a contact successfully"),  // success callback
//                failureResponse -> Log.i(TAG, "HomeActivity.onCreate(): contact failed with this response: " + failureResponse)  // failure callback
//        );
//

////        // Testing creating Amplify model class
//    String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
//    com.amplifyframework.datastore.generated.model.Product testProduct =
//      com.amplifyframework.datastore.generated.model.Product.builder()
//        .name("Product Name Here")  // required section, can't get to non-required properties yet
//        .description("It's a cool product")
//        .dateCreated(new Temporal.DateTime(currentDateString))
//        .productCategory("Clothes")
//        .build();
//    Amplify.API.mutate(
//      ModelMutation.create(testProduct),  // making a GraphQL request to the cloud
//      successResponse -> Log.i(TAG, "ProductListActivity.onCreate(): made a product successfully"),  // success callback
//      failureResponse -> Log.i(TAG, "ProductListActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
//    );


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
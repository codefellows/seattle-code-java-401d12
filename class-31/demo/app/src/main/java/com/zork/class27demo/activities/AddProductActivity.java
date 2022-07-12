package com.zork.class27demo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Contact;
import com.amplifyframework.datastore.generated.model.Product;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.zork.class27demo.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddProductActivity extends AppCompatActivity {
// WARNING: Be careful of using CompletableFuture in runOnUiThread()! Sometimes it seems to break
    // Also, I recommend using a future only in a single Activity, not between activities

    public static final String TAG = "AddProductActivity";
    String[] categories = {"Clothes", "Electronics", "Perishable Goods", "Office Supplies", "Misc"};
    Spinner contactSpinner = null;
    CompletableFuture<List<Contact>> contactsFuture = null;
    FusedLocationProviderClient fusedLocationClient = null;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        contactsFuture = new CompletableFuture<>();

        //Step 2: request permissions
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);  // hardcoded to 1; you can pick anything between 1 and 65535
        // Step 3: give fusedLocationClient value
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationClient.flushLocations();


        setUpSpinner();
        setUpSaveButton();

    }


    private void setUpSpinner() {
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        contactSpinner = findViewById(R.id.addAProductContactSpinner);
        Amplify.API.query(
                ModelQuery.list(Contact.class),
                success -> {
                    Log.i(TAG, "Read Contacts successfully!");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Contact> contacts = new ArrayList<>();

                    for (Contact contact : success.getData()) {
                        contacts.add(contact);
                        contactNames.add(contact.getFullName());
                    }
                    contactsFuture.complete(contacts);

                    runOnUiThread(() -> {
                        contactSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                contactNames));
                    });
                },
                failure -> {
                    contactsFuture.complete(null);
                    Log.e(TAG, "Did not read contacts successfully");
                }
        );
        productCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                // TODO: get rid of enum
                categories));
    }

    private void setUpSaveButton() {
        Spinner productCategorySpinner = findViewById(R.id.addAProductCategoryInput);
        Button saveNewProductButton = findViewById(R.id.addAProductSaveButton);
        saveNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = ((EditText) findViewById(R.id.addAProductNameInput)).getText().toString();
                String productDescription = ((EditText) findViewById(R.id.addAProductDescriptionInput)).getText().toString();
                String selectedContactString = contactSpinner.getSelectedItem().toString();
                // AWS time util
                String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

                List<Contact> contacts = null;
                try {
                    contacts = contactsFuture.get();
                } catch (InterruptedException ie) {
                    Log.e(TAG, "InterruptedException while getting contacts");
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ee) {
                    Log.e(TAG, "ExecvutionException while getting contacts");
                }
                Contact selectedContact = contacts.stream()
                        .filter(c -> c.getFullName().equals(selectedContactString)).findAny().orElseThrow(RuntimeException::new);
//                saveProduct(productName, productDescription, currentDateString, productCategorySpinner, selectedContact);

                if (ActivityCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.e(TAG, "Application does not have access to either ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION!");
                    return;
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location == null) {
                        Log.e(TAG, "Location was null");
                    }
                    String currentLatitude = Double.toString(location.getLatitude());
                    String currentLongitude = Double.toString(location.getLongitude());
                    Log.i(TAG, "Our Latitude: " + currentLatitude);
                    Log.i(TAG, "Our Longitude: " + currentLongitude);
                });
            }
        });
    }

    public void saveProduct(String productName, String productDescription, String currentDateString, Spinner productCategorySpinner, Contact selectedContact){
        // TODO: LOOK AT THE MODEL BUILDER
        Product newProduct = Product.builder()
                .name(productName)
                .description(productDescription)
                .dateCreated(new Temporal.DateTime(currentDateString))
                .productCategory(productCategorySpinner.getSelectedItem().toString())
                .contactPerson(selectedContact)
                .build();
        // TODO: THIS IS HOW WE CUD DynamoDB
        Amplify.API.mutate(
                ModelMutation.create(newProduct), // making a Graphql request to the cloud
                // ModelMutation.update()
                //ModelMutation.delete()
                successResponse -> Log.i(TAG, "AddProductActivity.onCreate(): made a product successfully"),  // success callback
                failureResponse -> Log.i(TAG, "AddProductActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
        );
    }
}

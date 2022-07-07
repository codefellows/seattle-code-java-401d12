package com.zork.class27demo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Contact;
import com.amplifyframework.datastore.generated.model.Product;
import com.google.android.material.snackbar.Snackbar;
import com.zork.class27demo.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderFormActivity extends AppCompatActivity {
    public static final String TAG = "orderFormActivity";
    private Product productToEdit = null;
    private CompletableFuture<Product> productCompletableFuture = null;
    private Spinner productCategorySpinner = null;
    private Spinner contactSpinner = null;
    private CompletableFuture<List<Contact>> contactsFuture = null;
    private EditText nameEditText;
    private EditText descriptionEditText;
    String[] categories = {"Clothes", "Electronics", "Perishable Goods", "Office Supplies", "Misc"};
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        productCompletableFuture = new CompletableFuture<>();
        contactsFuture = new CompletableFuture<>();
        // WARNING: The ActivityResultLauncher MUST be initialized in onCreate(), not in onResume() or a click handler! Otherwise it will fail
        activityResultLauncher = getImagePickingActivityResultLauncher();

        SetUpEditableUiElements();
        setUpSaveButton();
        setUpDeleteButton();
        setUpAddImageButton();
    }

    public void setUpAddImageButton(){
        Button addImageButton = findViewById(R.id.orderFormAddImageButton);
        addImageButton.setOnClickListener(v -> {
            launchImageSelectionIntent();
        });
    }

    public void launchImageSelectionIntent(){
        // Part 1: Launch Activity to pick a file
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);  // one of several file picking activities built into Android
        imageFilePickingIntent.setType("*/*");  // only allow one kind or category of file; if you don't have this, you get a very cryptic error about "No activity found to handle Intent"
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});  // only pick JPEG and PNG images

        // Launch Android's built-in file picking activity using our newly created ActivityResultLauncher below
        activityResultLauncher.launch(imageFilePickingIntent);
    }

    public ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                Uri pickedImageURI = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageURI);
                                    String pickedImageFileName = getFileNameFromUri(pickedImageURI);
                                    // TODO:
                                    //uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from a file on our phone");
                                } catch (FileNotFoundException fnfe) {
                                    Log.e(TAG, "Could not get file from phone: " + fnfe.getMessage(), fnfe);
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void SetUpEditableUiElements() {
        Intent callingIntent = getIntent();
        String productId = null;
        if (callingIntent != null) {
            productId = callingIntent.getStringExtra(HomeActivity.PRODUCT_ID_TAG);
        }
        String productId2 = productId;
        Amplify.API.query(
                ModelQuery.list(Product.class),
                success -> {
                    Log.i(TAG, "Read products successfully");
                    for (Product product : success.getData()) {
                        if (product.getId().equals(productId2)) {
                            productCompletableFuture.complete(product);
                        }
                    }
                    runOnUiThread(() -> {
                        // Update ui elements here
                    });
                },
                failure -> Log.i(TAG, "Did not read products successfully")
        );
        try {
            productToEdit = productCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting product");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting product");
        }

        nameEditText = ((EditText) findViewById(R.id.editProductProductNameEditText));
        nameEditText.setText(productToEdit.getName());
        descriptionEditText = ((EditText) findViewById(R.id.editProductDescriptionEditText));
        descriptionEditText.setText(productToEdit.getDescription());
        setUpSpinners();
    }

    private void setUpSpinners() {
        contactSpinner = (Spinner) findViewById(R.id.editProductContactSpinner);

        Amplify.API.query(
                ModelQuery.list(Contact.class),
                success ->
                {
                    Log.i(TAG, "Read contacts successfully!");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Contact> contacts = new ArrayList<>();
                    for (Contact contact : success.getData()) {
                        contacts.add(contact);
                        contactNames.add(contact.getFullName());
                    }
                    contactsFuture.complete(contacts);

                    runOnUiThread(() ->
                    {
                        contactSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames));
//                        contactSpinner.setSelection(getSpinnerIndex(contactSpinner, productToEdit.getContactPerson().getFullName()));
                    });
                },
                failure -> {
                    contactsFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!");
                }
        );

        productCategorySpinner = (Spinner) findViewById(R.id.editProductCategorySpinner);
        productCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories));
        productCategorySpinner.setSelection(getSpinnerIndex(productCategorySpinner, productToEdit.getProductCategory()));
    }

    private void setUpSaveButton() {
        Button saveButton = (Button) findViewById(R.id.editProductSaveButton);
        saveButton.setOnClickListener(v ->
        {
            List<Contact> contacts = null;
            String contactToSaveString = contactSpinner.getSelectedItem().toString();
            try {
                contacts = contactsFuture.get();
            } catch (InterruptedException ie) {
                Log.e(TAG, "InterruptedException while getting product");
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee) {
                Log.e(TAG, "ExecutionException while getting product");
            }
            Contact contactToSave = contacts.stream().filter(c -> c.getFullName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);
            Product productToSave = Product.builder()
                    .name(nameEditText.getText().toString())
                    .id(productToEdit.getId())
                    .dateCreated(productToEdit.getDateCreated())
                    .description(descriptionEditText.getText().toString())
                    .contactPerson(contactToSave)
                    .productCategory(productCategorySpinner.getSelectedItem().toString())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.update(productToSave),  // making a GraphQL request to the cloud
                    successResponse ->
                    {
                        Log.i(TAG, "OrderFormActivity.onCreate(): edited a product successfully");
                        // TODO: Display a Snackbar
                        Snackbar.make(findViewById(R.id.OrderFormActivity), "Product saved!", Snackbar.LENGTH_SHORT).show();
                    },  // success callback
                    failureResponse -> Log.i(TAG, "OrderFormActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
            );
        });
    }

    private void setUpDeleteButton() {
        Button deleteButton = (Button) findViewById(R.id.editProductDeleteButton);
        deleteButton.setOnClickListener(v ->
        {
            Amplify.API.mutate(
                    ModelMutation.delete(productToEdit),
                    successResponse ->
                    {
                        Log.i(TAG, "EditProductActivity.onCreate(): deleted a product successfully");
                        Intent goToProductListActivity = new Intent(OrderFormActivity.this, HomeActivity.class);
                        startActivity(goToProductListActivity);
                    },  // success callback
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }

        return 0;
    }

}
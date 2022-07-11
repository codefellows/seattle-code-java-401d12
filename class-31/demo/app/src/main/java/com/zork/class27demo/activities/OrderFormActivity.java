package com.zork.class27demo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Contact;
import com.amplifyframework.datastore.generated.model.Product;
import com.google.android.material.snackbar.Snackbar;
import com.zork.class27demo.R;

import java.io.File;
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
    private String s3ImageKey = "";
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

        s3ImageKey = productToEdit.getProductImageKey();
        if (s3ImageKey != null && !s3ImageKey.isEmpty()){
            Amplify.Storage.downloadFile(
                    s3ImageKey,
                    new File(getApplication().getFilesDir(), s3ImageKey),
                    success -> {
                        ImageView productImageView = findViewById(R.id.orderFormImageView);
                        productImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> Log.e(TAG, "Unable to get image from S3 fpr the product with s3 key: " + s3ImageKey + "with error: " + failure.getMessage(), failure)
            );
        }

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
                                Uri pickedImageUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageUri);
                                    String pickedImageFileName = getFileNameFromUri(pickedImageUri);
                                    // TODO:
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFileName, pickedImageUri);
                                    Log.i(TAG, "Succeeded in getting input stream from a file on our phone");
                                } catch (FileNotFoundException fnfe) {
                                    Log.e(TAG, "Could not get file from phone: " + fnfe.getMessage(), fnfe);
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    public void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFileName, Uri pickedImageUri){
        Amplify.Storage.uploadInputStream(
                pickedImageFileName,
                pickedImageInputStream,
                success -> {
                    // Log the success
                    Log.i(TAG, "Succeeded in upoloading file to s3: " + success.getKey());
                    // grab hold of the s3key
                    s3ImageKey = success.getKey();

                    // grab thew image view to display too
                    ImageView productImageView = findViewById(R.id.orderFormImageView);
                    // need a copy of InputStream, because you can't reuse them(just like a queue)
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        // Reading a uri to get the input stream again
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get input stream from uri: " + fnfe.getMessage(), fnfe);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFileName + " with error: " + failure.getMessage())
        );
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
            saveProduct();
        });
    }

    public void saveProduct(){
        // setup a List of conatacts
        List<Contact> contacts = null;
        // Grab from contact spinner, the current selected contact
        String contactToSaveString = contactSpinner.getSelectedItem().toString();
        // try catch for completebale future
        try {
            contacts = contactsFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting product");
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting product");
        }
        // Contact and Product builder
        Contact contactToSave = contacts.stream().filter(contact -> contact.getFullName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);
        Product productToSave = Product.builder()
                .name(nameEditText.getText().toString())
                .id(productToEdit.getId())
                .dateCreated(productToEdit.getDateCreated())
                .description(productToEdit.getDescription())
                .contactPerson(contactToSave)
                .productCategory(productCategorySpinner.getSelectedItem().toString())
                .productImageKey(s3ImageKey)
                .build();
//        AMplify update call
        Amplify.API.mutate(
                ModelMutation.update(productToSave),
                success -> {
                    Log.i(TAG, "Product updated successfully");
                    //TODO: Make a success toast
                },
                failure -> Log.e(TAG, "Product did not update")
        );
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
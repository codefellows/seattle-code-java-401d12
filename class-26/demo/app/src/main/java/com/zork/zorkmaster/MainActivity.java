package com.zork.zorkmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// just like ouir main() -- entry point
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // element by ID
        Button submitButton = MainActivity.this.findViewById(R.id.mainSubmitButton);

        // clickIT! set the listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("I AM THE LISTENER!!!!!!");
                Log.v("", "Very VERBOSE!!!");
                Log.d("", "DEBUG");
                Log.i("", "INFORMATION!!");
                Log.w("", "WARNING!!");
                Log.e("", "ERROR");
                Log.wtf("", "WHAT A TERRIBLE FAILURE");
                TextView greeting = MainActivity.this.findViewById(R.id.mainGreeting);
                greeting.setText("I am changed");
            }
        });
        //callback

        // grab the button
    Button orderFormButton = MainActivity.this.findViewById(R.id.goToOrderFormButton);
    // setting up route logic, intents are the highway between Activities
    orderFormButton.setOnClickListener(v -> {
        Intent goToOrderFromIntent = new Intent(MainActivity.this, OrderForm.class);
        startActivity(goToOrderFromIntent);
        // Need class context we are coming from, and class we are going to, so we can navigate back after
    });

    }
    // JS adding event listener
    //1. Get a UI element by id
    //2. add an event listener
    //3. Callback fn OnClick -> do stuff
    //4. do stuff in the callback


    public void CreateHelper(){

    }
}
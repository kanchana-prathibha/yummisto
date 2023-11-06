package com.coffeeprogrammer.yummisto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.coffeeprogrammer.yummisto.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {
    private TextView selectedQuantityTextView;
    private TextView selectedAmountTextView;
    private Button btnSubmitOrder;

    private EditText customerNameEditText,customerAddressEditText,customerContactEditText;
    private TextView cakeNameTextView;
    private TextView cakePriceTextView;
    private TextView cakeWeightTextView;
    // Initialize selected quantity and cake price (you can get the actual values)
    private int selectedQuantity = 0;
    private double cakePrice = 0.00;

    private double finalPrice = 0.0;
    private ImageView imageViewCake;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private String cakeId;
    private void submitOrder() {
        // Get the customer details from the EditText fields
        String customerName = customerNameEditText.getText().toString().trim();
        String customerAddress = customerAddressEditText.getText().toString().trim();
        String customerContact = customerContactEditText.getText().toString().trim();
        String customerEmail = mAuth.getCurrentUser().getEmail(); // Get customer email from Firebase Auth
         cakeId = getIntent().getStringExtra("cake_id"); // Get cake ID from the Intent

        // Validate customer details
        if (customerName.isEmpty() || customerAddress.isEmpty() || customerContact.isEmpty()) {
            Toast.makeText(this, "Please fill in all customer details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Order object
        Order order = new Order(customerEmail, cakeId, customerName, customerAddress, customerContact,
                cakeNameTextView.getText().toString(), selectedQuantity, finalPrice);

        // Get a reference to the "orders" node in the Firebase database
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Push the order to the database
        String orderKey = ordersRef.push().getKey(); // Generate a unique key for the order
        ordersRef.child(orderKey).setValue(order);

        // Display a success message
        Toast.makeText(this, "Order submitted successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to the main activity or any other appropriate screen
        Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Close all activities on top of the main activity
        startActivity(intent);
        finish(); // Finish the OrderActivity
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize the views
        selectedQuantityTextView = findViewById(R.id.selectedQuantityTextView);
        selectedAmountTextView = findViewById(R.id.cakePriceTextView);
        btnSubmitOrder = findViewById(R.id.submitOrderButton);

        mAuth = FirebaseAuth.getInstance();
        customerAddressEditText = findViewById(R.id.customerAddressEditText);
        cakeNameTextView = findViewById(R.id.cakeNameTextView);
        imageViewCake = findViewById(R.id.selectedCakeImageView);
        cakePriceTextView = findViewById(R.id.cakePriceTextView);
        customerContactEditText = findViewById(R.id.customerContactEditText);
        cakeWeightTextView = findViewById(R.id.cakeWeightTextView);

        // Submit the order when the user clicks the "Submit Order" button
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle order submission
                submitOrder();
            }
        });

        customerNameEditText = findViewById(R.id.customerNameEditText);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        // Query the "users" node to find the user with the specified email
        String email = mAuth.getCurrentUser().getEmail();
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Iterate through the result to find the user
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String firstName = userSnapshot.child("first_name").getValue(String.class);
                        String lastName = userSnapshot.child("last_name").getValue(String.class);

                        customerNameEditText.setText(firstName + " " + lastName);

                        // Notify the listener with the user's name

                        return; // Exit the loop once a matching user is found
                    }
                }

                // Handle the case when the user is not found

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Retrieve cake details from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String cakeName = intent.getExtras().getString("cake_name","");
            Double cakePrice = intent.getExtras().getDouble("cake_price", 0.0);
            Double cakeWeight = intent.getExtras().getDouble("cake_weight", 0.0);

            String image_url = intent.getExtras().getString("image","");
            Glide.with(this).load(image_url).into(imageViewCake);
            selectedQuantity = intent.getExtras().getInt("selectedQuantity",0);

            selectedQuantityTextView.setText("Selected quantity: "+ String.valueOf(selectedQuantity));
            // Update the TextView elements with the cake details
            cakeNameTextView.setText(cakeName);
        // Toast.makeText(this, cakePrice.toString(), Toast.LENGTH_SHORT).show();
            cakePriceTextView.setText("Price: LKR " + String.valueOf(cakePrice));
            cakeWeightTextView.setText("Weight: " + cakeWeight + " kg");
            // Calculate and update the final amount
            updateFinalAmount(cakePrice, selectedQuantity);

        }


    }
    private void updateFinalAmount(double cakePrice, int quantity) {
        // Calculate the final amount based on cake price and quantity
        double finalAmount = cakePrice * quantity;

        // Find and update the "Final Amount" TextView
        TextView finalAmountTextView = findViewById(R.id.finalAmountTextView);
        finalAmountTextView.setText("Final Amount: LKR " + finalAmount);
    }


}

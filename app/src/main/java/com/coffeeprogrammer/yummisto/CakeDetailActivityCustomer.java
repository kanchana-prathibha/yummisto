package com.coffeeprogrammer.yummisto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coffeeprogrammer.yummisto.models.Cake;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CakeDetailActivityCustomer extends AppCompatActivity {

    private EditText cakeNameEditText;
    private EditText cakePriceEditText;
    private EditText cakeWeightEditText;
    private EditText cakeAvailableQuantityEditText, quantityEditText;

    private String cakeId; // The ID of the cake
    private String imgUrl, category_id;
    private Button btnBuyNow;
    private ImageView cakeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_detail_customer);
        cakeId = getIntent().getStringExtra("cake_id");

        cakeImage = findViewById(R.id.cakeImage);
        quantityEditText = findViewById(R.id.quantityEditText);
        // Initialize UI elements
        cakeNameEditText = findViewById(R.id.cakeNameEditText);
        cakePriceEditText = findViewById(R.id.cakePriceEditText);
        cakeWeightEditText = findViewById(R.id.cakeWeightEditText);
        cakeAvailableQuantityEditText = findViewById(R.id.cakeAvailableQuantityEditText);


        loadCakeDetails(cakeId);


        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected quantity
                String quantityText = quantityEditText.getText().toString();
                if (!quantityText.isEmpty()) {
                    int selectedQuantity = Integer.parseInt(quantityText);

                    if (Integer.parseInt(cakeAvailableQuantityEditText.getText().toString()) < selectedQuantity){
                        Toast.makeText(CakeDetailActivityCustomer.this, "Available quantity is less than you need.", Toast.LENGTH_SHORT).show();
                    }else{


                    }
                   Intent intent = new Intent(CakeDetailActivityCustomer.this, OrderActivity.class);
                    intent.putExtra("selectedQuantity", selectedQuantity); // Pass the selected quantity to the next activity
                    intent.putExtra("cake_name",cakeNameEditText.getText().toString());
                    intent.putExtra("cake_price",Double.parseDouble(cakePriceEditText.getText().toString()));
                   // Toast.makeText(CakeDetailActivityCustomer.this, cakePriceEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("cake_weight",Double.parseDouble(cakeWeightEditText.getText().toString()));
                    intent.putExtra("image",imgUrl);

                    intent.putExtra("cake_id",cakeId);
                    startActivity(intent);
                } else {
                    // Handle the case where the quantity is not entered by the user.
                    // You can display an error message or a toast to prompt the user to enter a quantity.
                    Toast.makeText(CakeDetailActivityCustomer.this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void loadCakeDetails(String cakeId) {
        // Assuming you have a Firebase reference for cakes
        DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("cakes");

        // Query the Firebase database to get the specific cake with the given cakeId
        cakesRef.child(cakeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Cake details found, retrieve them
                    Cake cake = dataSnapshot.getValue(Cake.class);

                    // Update the UI elements with the retrieved details
                    cakeNameEditText.setText(cake.getName());
                    cakePriceEditText.setText(String.valueOf(cake.getPrice()));
                    cakeWeightEditText.setText(String.valueOf(cake.getWeight()));
                    cakeAvailableQuantityEditText.setText(String.valueOf(cake.getAvailableQuantity()));
                    imgUrl = String.valueOf(cake.getImageUrl());
                   // Toast.makeText(CakeDetailActivityCustomer.this, imgUrl, Toast.LENGTH_SHORT).show();
                    category_id = String.valueOf(cake.getCategoryId());
                    // Load cake image using Glide
                    Glide.with(CakeDetailActivityCustomer.this).load(imgUrl).into(cakeImage);

                } else {
                    // Handle the case where the cake is not found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

}
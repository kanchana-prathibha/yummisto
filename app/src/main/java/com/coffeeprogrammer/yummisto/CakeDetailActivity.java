package com.coffeeprogrammer.yummisto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coffeeprogrammer.yummisto.models.Cake;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CakeDetailActivity extends AppCompatActivity {

    private EditText cakeNameEditText;
    private EditText cakePriceEditText;
    private EditText cakeWeightEditText;
    private EditText cakeAvailableQuantityEditText;

    private String cakeId; // The ID of the cake
    private String imgUrl, category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_detail);
        String cakeId = getIntent().getStringExtra("cake_id");

        // Initialize UI elements
        cakeNameEditText = findViewById(R.id.cakeNameEditText);
        cakePriceEditText = findViewById(R.id.cakePriceEditText);
        cakeWeightEditText = findViewById(R.id.cakeWeightEditText);
        cakeAvailableQuantityEditText = findViewById(R.id.cakeAvailableQuantityEditText);

        Button updateCakeButton = findViewById(R.id.updateCakeButton);
        Button deleteCakeButton = findViewById(R.id.deleteCakeButton);

        loadCakeDetails(cakeId);


        deleteCakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a confirmation dialog to ask the user if they want to delete the cake
                AlertDialog.Builder builder = new AlertDialog.Builder(CakeDetailActivity.this);
                builder.setMessage("Are you sure you want to delete this cake?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, proceed with deletion
                        DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("cakes");

                        // Delete the specific cake with the provided cakeId
                        cakesRef.child(cakeId).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    // Deletion successful
                                    finish(); // Optionally, close the activity after deleting
                                } else {
                                    // Handle the deletion error
                                    // You may show an error message or log the error
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled, do nothing
                    }
                });
                builder.show();
            }
        });

        updateCakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract updated values from the UI
                String updatedCakeName = cakeNameEditText.getText().toString();
                double updatedCakePrice = Double.parseDouble(cakePriceEditText.getText().toString());
                double updatedCakeWeight = Double.parseDouble(cakeWeightEditText.getText().toString());

                // Extract the updated available quantity from the UI
                int updatedAvailableQuantity = Integer.parseInt(cakeAvailableQuantityEditText.getText().toString());
                // Update the cake details in Firebase Realtime Database
               // Cake updatedCake = new Cake(cakeId, updatedCakeName, updatedCakePrice, updatedCakeWeight, updatedAvailableQuantity, imgUrl, category_id);


                DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("cakes");



                // Construct the update object
                Map<String, Object> updateValues = new HashMap<>();
                updateValues.put("name", updatedCakeName);
                updateValues.put("price", updatedCakePrice);
                updateValues.put("weight", updatedCakeWeight);
                updateValues.put("availableQuantity", updatedAvailableQuantity);

                // Update the specific cake with the provided cakeId
                cakesRef.child(cakeId).updateChildren(updateValues, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Update successful
                            finish(); // Optionally, close the activity after updating
                        } else {
                            // Handle the update error
                            // You may show an error message or log the error
                        }
                    }
                });
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
                    category_id = String.valueOf(cake.getCategoryId());
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
package com.coffeeprogrammer.yummisto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.coffeeprogrammer.yummisto.models.Cake;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddCakeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView cakeImageView;
    private Button selectImageButton;
    private EditText cakeNameEditText;
    private EditText cakePriceEditText;
    private EditText cakeWeightEditText;
    private EditText availableQuantityEditText;
    private Button saveCakeButton;

    private Uri imageUri; // Stores the selected image URI
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference cakesRef;
    private String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Get the category ID from the Intent (you can pass it from the previous activity)
        categoryId = getIntent().getStringExtra("categoryId");

        // Initialize Firebase Realtime Database (Assuming you have a "cakes" node)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cakesRef = database.getReference("cakes");
        cakeImageView = findViewById(R.id.cakeImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        cakeNameEditText = findViewById(R.id.cakeNameEditText);
        cakePriceEditText = findViewById(R.id.cakePriceEditText);
        cakeWeightEditText = findViewById(R.id.cakeWeightEditText);
        availableQuantityEditText = findViewById(R.id.availableQuantityEditText);
        saveCakeButton = findViewById(R.id.saveCakeButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveCakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an image is selected
                if (imageUri != null) {
                    // Create and show a progress dialog
                    final ProgressDialog progressDialog = new ProgressDialog(AddCakeActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Saving Cake...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // Get a reference to the Storage location where you want to store the cake image
                    StorageReference imageRef = storageReference.child("cake_images/" + UUID.randomUUID().toString());

                    // Upload the image to Firebase Storage
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrl = uri.toString();

                                            // Get cake details from EditText fields
                                            String cakeName = cakeNameEditText.getText().toString();
                                            double cakePrice = Double.parseDouble(cakePriceEditText.getText().toString());
                                            double cakeWeight = Double.parseDouble(cakeWeightEditText.getText().toString());
                                            int availableQuantity = Integer.parseInt(availableQuantityEditText.getText().toString());

                                            String cake_id =  cakesRef.push().getKey();

                                            // Create a Cake object with the details
                                            Cake newCake = new Cake(
                                                    cake_id,
                                                    cakeName,
                                                    cakePrice,
                                                    cakeWeight,
                                                    availableQuantity,
                                                    downloadUrl,
                                                    categoryId // Set the categoryId
                                            );

                                            cakesRef.child(cake_id).setValue(newCake);


                                            // Dismiss the progress dialog
                                            progressDialog.dismiss();

                                            // Inform the user that the cake has been added
                                            Toast.makeText(AddCakeActivity.this, "Cake added successfully", Toast.LENGTH_SHORT).show();

                                            // Finish the activity or navigate to another screen as needed
                                            finish();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the failure of image upload
                                    progressDialog.dismiss(); // Dismiss the progress dialog in case of failure
                                    Toast.makeText(AddCakeActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AddCakeActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Opens the image picker to select an image from the device
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handles the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            cakeImageView.setImageURI(imageUri);
        }
    }
}

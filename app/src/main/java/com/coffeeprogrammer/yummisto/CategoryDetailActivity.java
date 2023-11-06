package com.coffeeprogrammer.yummisto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coffeeprogrammer.yummisto.adapters.CakeAdapter;
import com.coffeeprogrammer.yummisto.models.Cake;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {

    private TextView categoryNameTextView;
    private Button updateCategoryButton;
    private Button deleteCategoryButton;
    private Button addCakeButton;
    private String categoryName, categoryId;

    private RecyclerView recyclerView;
    private CakeAdapter cakeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);


        categoryNameTextView = findViewById(R.id.categoryNameTextView);
        updateCategoryButton = findViewById(R.id.updateCategoryButton);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);
        addCakeButton = findViewById(R.id.addCakeButton);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        categoryId  = intent.getStringExtra("categoryId");
        categoryNameTextView.setText(categoryName);
        updateCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement update category logic here
                // You can open a new activity or dialog to edit the category details
                // Show a dialog to update the category name
                showUpdateCategoryDialog();
            }
        });

        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(CategoryDetailActivity.this);
                confirmDialog.setTitle("Confirm Deletion");
                confirmDialog.setMessage("Are you sure you want to delete this category and all its associated cakes?");
                confirmDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the category and associated cakes
                        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("cake_categories").child(categoryId); // Replace categoryId with the actual category ID
                        categoryRef.removeValue(); // Delete the category node

                        // Delete associated cakes
                        DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("cakes").child(categoryId); // Assuming you have a "cakes" node
                        cakesRef.removeValue(); // Delete the associated cakes node

                        // Finish the current activity
                        finish();
                    }
                });
                confirmDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmDialog.show();
            }
        });
        recyclerView = findViewById(R.id.recyclerViewCakes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cakeAdapter = new CakeAdapter();
        // Query Firebase Realtime Database to retrieve cakes for the selected category (using the categoryId)
        DatabaseReference cakesRef = FirebaseDatabase.getInstance().getReference("cakes");
        Query query = cakesRef.orderByChild("categoryId").equalTo(categoryId); // Filter by categoryId
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Cake> cakes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cake cake = snapshot.getValue(Cake.class);
                    if (cake != null) {
                        cakes.add(cake);
                    }
                }
                cakeAdapter.setCakes(cakes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        recyclerView.setAdapter(cakeAdapter);

        addCakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement add cake logic here
                // Start a new activity to add a cake to this category
                Intent intent = new Intent(CategoryDetailActivity.this, AddCakeActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("categoryId", categoryId);
                startActivity(intent);
            }
        });

    }
    private void showUpdateCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Category");

        // Create an EditText to enter the new category name
        final EditText editText = new EditText(this);
        editText.setText(categoryName); // Set the current category name
        builder.setView(editText);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedCategoryName = editText.getText().toString().trim();
                if (!updatedCategoryName.isEmpty()) {
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("cake_categories").child(categoryId); // Replace categoryId with the actual category ID
                    categoryRef.child("name").setValue(updatedCategoryName);

                    categoryNameTextView.setText(updatedCategoryName); // Update the displayed category name
                } else {
                    Toast.makeText(CategoryDetailActivity.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
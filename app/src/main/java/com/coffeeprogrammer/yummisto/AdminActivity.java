package com.coffeeprogrammer.yummisto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coffeeprogrammer.yummisto.adapters.CategoryAdapter;
import com.coffeeprogrammer.yummisto.models.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private EditText categoryNameEditText;
    private Button createButton;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    private FirebaseDatabase database;
    private DatabaseReference categoriesRef;

    private List<Category> categories = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform the logout action here
                        FirebaseAuth.getInstance().signOut();

                        // Close the current activity and open the MainActivity
                        finish();
                        startActivity(new Intent(AdminActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Cancel" button, close the dialog
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryNameEditText = findViewById(R.id.editTextCategoryName);
        createButton = findViewById(R.id.buttonCreateCategory);
        recyclerView = findViewById(R.id.recyclerViewCategories);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("cake_categories");

        // Set up the RecyclerView
        categoryAdapter = new CategoryAdapter(categories);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Start the CategoryDetailActivity and pass the category details
                Intent intent = new Intent(AdminActivity.this, CategoryDetailActivity.class);
                intent.putExtra("categoryId", category.getId());
                intent.putExtra("categoryName", category.getName());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryAdapter);


        // Read and display existing categories
        readCategories();



        // Create a new category when the button is clicked
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameEditText.getText().toString().trim();
                if (!categoryName.isEmpty()) {
                    createCategory(categoryName);
                    categoryNameEditText.setText("");
                } else {
                    Toast.makeText(AdminActivity.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createCategory(String categoryName) {
        String categoryId = categoriesRef.push().getKey();
        Category category = new Category(categoryId, categoryName);
        if (categoryId != null) {
            categoriesRef.child(categoryId).setValue(category);
        }
    }

    private void readCategories() {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    if (category != null) {
                        categories.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Failed to read categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
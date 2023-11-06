package com.coffeeprogrammer.yummisto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.coffeeprogrammer.yummisto.adapters.CakeAdapter;
import com.coffeeprogrammer.yummisto.adapters.CakeAdapterCustomer;
import com.coffeeprogrammer.yummisto.models.Cake;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerCategoryDetail extends AppCompatActivity {

    private TextView categoryNameTextViewCustomer;
    private String categoryName, categoryId;
    private RecyclerView recyclerView;
    private CakeAdapterCustomer cakeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category_detail);
        categoryNameTextViewCustomer = findViewById(R.id.categoryNameTextViewCustomer);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
        categoryId  = intent.getStringExtra("category_id");
        categoryNameTextViewCustomer.setText(categoryName);
        recyclerView = findViewById(R.id.recyclerViewCakesCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cakeAdapter = new CakeAdapterCustomer();
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


    }
}
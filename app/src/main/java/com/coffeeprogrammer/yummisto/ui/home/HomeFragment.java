package com.coffeeprogrammer.yummisto.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coffeeprogrammer.yummisto.CustomerCategoryDetail;
import com.coffeeprogrammer.yummisto.adapters.CategoryAdapter;
import com.coffeeprogrammer.yummisto.databinding.FragmentHomeBinding;
import com.coffeeprogrammer.yummisto.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    private List<Category> categories; // Load categories here
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();


        categoryRecyclerView =  binding.categoryRecyclerView;
        categoryAdapter = new CategoryAdapter(categories);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Handle item click, e.g., open a new activity for category details
            }
        });
        // Load your category data into the 'categories' list
        categories = loadCategories();

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//
        return root;
    }

    private List<Category> loadCategories() {
        List<Category> categories = new ArrayList<>();

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("cake_categories");

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
               // categoryAdapter.setCategories(categories);
                categoryAdapter = new CategoryAdapter(categories);
                categoryRecyclerView.setAdapter(categoryAdapter);
                categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Category category) {
                        Intent intent = new Intent(getActivity(), CustomerCategoryDetail.class);
                        intent.putExtra("category_id",category.getId());

                        intent.putExtra("categoryName",category.getName());

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or failures to load categories
            }
        });

        return categories;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
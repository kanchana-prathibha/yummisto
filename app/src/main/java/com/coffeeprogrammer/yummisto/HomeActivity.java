package com.coffeeprogrammer.yummisto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.coffeeprogrammer.yummisto.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    public static FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     binding = ActivityHomeBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        View headView = navigationView.getHeaderView(0);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        Button logoutBtn = headView.findViewById(R.id.btnLogout);
        TextView txtViewEmail = headView.findViewById(R.id.textViewEmail);

        TextView txtViewName = headView.findViewById(R.id.nameTxt);
       try {
           txtViewEmail.setText(mAuth.getCurrentUser().getEmail());


           // Query the "users" node to find the user with the specified email
           usersRef.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                     //  Toast.makeText(HomeActivity.this, "hi", Toast.LENGTH_SHORT).show();

                       // Iterate through the result to find the user
                       for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                           String firstName = userSnapshot.child("first_name").getValue(String.class);
                           String lastName = userSnapshot.child("last_name").getValue(String.class);

                           txtViewName.setText(firstName + " " + lastName);
                           // Notify the listener with the user's name
                           //   listener.onUserFound(firstName, lastName);
                           return; // Exit the loop once a matching user is found
                       }
                   }

                   // Handle the case when the user is not found
                   //     listener.onUserNotFound();
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   // Handle database error
                   //  listener.onDatabaseError();
               }
           });
       }catch (Exception ex){}


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Do you want to Logout?");
                alertDialog.setMessage("Are you sure you want to perform this action");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //logout user
                        if(mAuth.getCurrentUser() != null){
                            mAuth.signOut();
                            startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        }

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.create().show();
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
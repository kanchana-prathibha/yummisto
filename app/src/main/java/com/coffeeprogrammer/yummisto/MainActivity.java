package com.coffeeprogrammer.yummisto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button btnSignin, btnSignup;
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        try{
            if(mAuth.getCurrentUser() != null){
                String user_email = mAuth.getCurrentUser().getEmail();
                DatabaseReference databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference();
                databaseReferenceAdmin.child("admin").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        if (snapshot.getValue().toString().equals(user_email)){
                            //user is admin
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                            finish();


                        }else{
                            //normal user
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }catch (Exception ex){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignin = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.editEmailLogin);
        editTextPassword = findViewById(R.id.editPasswordLogin);
        btnSignup = findViewById(R.id.btnRegister);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //perform login
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"No empty fields allowed",Toast.LENGTH_SHORT).show();

                }else{

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //success
                                String user_email = mAuth.getCurrentUser().getEmail();
                            //    Toast.makeText(MainActivity.this,user_email, Toast.LENGTH_SHORT).show();

                                DatabaseReference databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference();
                                databaseReferenceAdmin.child("admin").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                        if (snapshot.getValue().toString().equals(user_email)){
                                            //user is admin

                                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                            finish();

                                        }else{
                                            //normal user
                                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //  Toast.makeText(MainActivity.this, user_id, Toast.LENGTH_SHORT).show();
                            }else{
                                //fail
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
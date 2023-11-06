package com.coffeeprogrammer.yummisto;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText editEmailRegister, editPasswordRegister, editPasswordRegisterAgain, editFirstNameRegister, editLastNameRegister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editEmailRegister = findViewById(R.id.editEmailSignup);
        editPasswordRegister = findViewById(R.id.editPasswordSignup);
        editPasswordRegisterAgain = findViewById(R.id.editPasswordSignup);
        btnCreate = findViewById(R.id.btnCreateAccount);

        editFirstNameRegister = findViewById(R.id.editFirstNameSignup);
        editLastNameRegister = findViewById(R.id.editLastNameSignup);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editFirstNameRegister.getText().toString()) || TextUtils.isEmpty(editLastNameRegister.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "First name and Last name is required", Toast.LENGTH_SHORT).show();
                }else {
                    if (editPasswordRegister.getText().toString().equals(editPasswordRegisterAgain.getText().toString())) {
                        //perform login
                        String email, password;
                        email = editEmailRegister.getText().toString();
                        password = editPasswordRegister.getText().toString();
                        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                            Toast.makeText(RegisterActivity.this, "No empty fields allowed", Toast.LENGTH_SHORT).show();

                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration is completed ", Toast.LENGTH_SHORT).show();

                                        DatabaseReference mRefPersonalDetails = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference newUserChildRef = mRefPersonalDetails.child("users").push();
                                        newUserChildRef.child("first_name").setValue(editFirstNameRegister.getText().toString());
                                        newUserChildRef.child("last_name").setValue(editLastNameRegister.getText().toString());
                                        newUserChildRef.child("email").setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                finish();
                                            }
                                        });



                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration is fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
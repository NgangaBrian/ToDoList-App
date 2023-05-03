package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
        private EditText editTextTextEmailAddress;
        private EditText editTextTextPassword;
        private Button button;
        private Button button2;
        private FirebaseAuth mAuth;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
            editTextTextPassword = findViewById(R.id.editTextTextPassword);
            button = findViewById(R.id.button);
            mAuth = FirebaseAuth.getInstance();

            button2 = findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn(editTextTextEmailAddress.getText().toString(), editTextTextPassword.getText().toString());
                }
            });
        }
        private void signIn(String email, String password) {
            if (email.isEmpty()) {
                editTextTextEmailAddress.setError("Username is required");
                editTextTextEmailAddress.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                editTextTextPassword.setError("Password is required");
                editTextTextPassword.requestFocus();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
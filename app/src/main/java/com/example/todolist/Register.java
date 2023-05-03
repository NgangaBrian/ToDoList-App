package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText editTextTextPersonName;
    private EditText editTextTextEmailAddress2;
    private EditText editTextTextPassword2;
    private EditText editTextTextPassword3;
    private TextView textView;
    private Button button3;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        editTextTextPassword3 = findViewById(R.id.editTextTextPassword3);
        button3 = findViewById(R.id.button3);
        textView = findViewById(R.id.textView);


        editTextTextPassword3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextTextPassword3.getText().toString().equals(editTextTextPassword2.getText().toString())) {
                    textView.setText("Passwords match!");
                    textView.setTextColor(Color.GREEN);
                } else {
                    textView.setText("Passwords do not match!");
                    textView.setTextColor(Color.RED);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        FirebaseDatabase.getInstance().getReference();

        databaseRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String Name = editTextTextPersonName.getText().toString();
                String Email = editTextTextEmailAddress2.getText().toString();
                String Password = editTextTextPassword2.getText().toString();



                if (Name.isEmpty()) {
                    editTextTextPersonName.setError("Username is required");
                    editTextTextPersonName.requestFocus();
                    return;
                }
                if (Email.isEmpty()) {
                    editTextTextEmailAddress2.setError("Username is required");
                    editTextTextEmailAddress2.requestFocus();
                    return;
                }

                if (Password.isEmpty()) {
                    editTextTextPassword2.setError("Password is required");
                    editTextTextPassword2.requestFocus();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String userId = firebaseAuth.getCurrentUser().getUid();
                                            DatabaseReference userReference = databaseRef.child("users").child(userId);
                                            userReference.child("name").setValue(Name);
                                            userReference.child("email").setValue(Email);
                                            userReference.child("password").setValue(Password);

                                            // Create a unique database for the user using their user ID
                                            DatabaseReference userDatabaseReference = databaseRef.child("user_databases").child(userId);
                                            userDatabaseReference.setValue("This is the unique database for " + Name);

                                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

            }
        });
    }
}
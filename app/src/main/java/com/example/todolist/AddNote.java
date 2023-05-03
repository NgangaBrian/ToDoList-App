package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNote extends AppCompatActivity {
    private EditText Note;
    private Button button;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Note = findViewById(R.id.editTextTextMultiLine);
        button = findViewById(R.id.button5);

        FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = database.getReference("users").child(userId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = Note.getText().toString();

                userRef.child("Notes").push().setValue(new NewNote(note))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddNote.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNote.this, "Not Successful", Toast.LENGTH_SHORT).show();
                            }
                        });








            }
        });
    }
}
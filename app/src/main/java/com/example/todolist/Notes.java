package com.example.todolist;
import static android.content.ContentValues.TAG;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Notes extends BaseActivity {
    private FloatingActionButton fab;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setUpNavigationDrawer();

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Notes.this, AddNote.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = database.getReference("users").child(userId);
        databaseRef = userRef.child("Notes");



        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error reading data", error.toException());
            }
        });
    }

    private void populateData(DataSnapshot dataSnapshot) {
        LinearLayout mainLayout = findViewById(R.id.linear_layout);
        mainLayout.removeAllViews();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String note = snapshot.child("note").getValue(String.class);
            String key = snapshot.getKey();

            // Create a new LinearLayout to hold each data item
            LinearLayout linearLayout = new LinearLayout(Notes.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // Set a bottom margin of 16dp
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
            linearLayout.setLayoutParams(layoutParams);

            // Create a new TextView for the data item
            TextView textView = new TextView(Notes.this);
            textView.setText(note);
            textView.setTextSize(20);
            // Add the TextView to the LinearLayout
            linearLayout.addView(textView);

            // Create a new Delete button for the data item
            Button deleteButton = new Button(Notes.this);
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Remove the data item from the database when the Delete button is clicked
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Notes").child(key);
                    ref.removeValue();
                }
            });
            //
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add the Delete button to the LinearLayout
            linearLayout.addView(deleteButton);

            // Add the LinearLayout to the main layout
            mainLayout.addView(linearLayout);
        }
    }
}



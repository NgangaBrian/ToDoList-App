package com.example.todolist;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends BaseActivity {
    private FloatingActionButton fab;
    private DatabaseReference mDatabaseRef, databaseRef;
    private TextView mNameTextView;
    private TextView mAgeTextView;
    private CheckBox checkBox;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpNavigationDrawer();

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Home.this, ToDoAdd.class);
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
        databaseRef = userRef.child("ToDo");

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
            String todo = snapshot.child("todo").getValue(String.class);
            String date = snapshot.child("date").getValue(String.class);
            String status = snapshot.child("status").getValue(String.class);
            String key = snapshot.getKey();

            // Create a new LinearLayout to hold each data item
            LinearLayout linearLayout = new LinearLayout(Home.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // Set a bottom margin of 16dp
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.item_margin_bottom);
            linearLayout.setLayoutParams(layoutParams);

            // Create a new CheckBox for the data item
            CheckBox checkBox = new CheckBox(Home.this);
            checkBox.setText(todo + " (" + date + ")");
            // Set the checkbox state based on the status retrieved from the database
            checkBox.setChecked(status != null && status.equals("finished"));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update the status in the database when the CheckBox is checked or unchecked
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("ToDo").child(key);
                    if (isChecked) {
                        ref.child("status").setValue("finished");
                    } else {
                        ref.child("status").removeValue();
                    }
                }
            });

            // Create a new Delete button for the data item
            Button deleteButton = new Button(Home.this);
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Remove the data item from the database when the Delete button is clicked
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("ToDo").child(key);
                    ref.removeValue();
                }
            });



            LinearLayout layout = new LinearLayout(Home.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(checkBox);
            layout.addView(deleteButton);
            // Add the CheckBox and Delete button to the LinearLayout
            linearLayout.addView(layout);

            // Add the LinearLayout to the main layout
            mainLayout.addView(linearLayout);
        }
    }
}

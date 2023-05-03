package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ToDoAdd extends AppCompatActivity {
    private EditText ToDo, Date;
    private Calendar myCalender;
    private Button button;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_add);

        Date = findViewById(R.id.editTextDate);
        ToDo = findViewById(R.id.editTextTextPersonName2);
        myCalender = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalender.set(Calendar.YEAR,year);
                myCalender.set(Calendar.MONTH,month);
                myCalender.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        Date.setOnClickListener(View ->{
            new DatePickerDialog(ToDoAdd.this,date,
                    myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH)).show();
        });

        button = findViewById(R.id.button4);

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
                String date = Date.getText().toString();
                String todo = ToDo.getText().toString();

                userRef.child("ToDo").push().setValue(new ToBeDone(date, todo))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ToDoAdd.this, "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ToDoAdd.this, "Not Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });








            }
        });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        Date.setText(dateFormat.format(myCalender.getTime()));
    }


}

    

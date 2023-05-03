package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Schedule extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setUpNavigationDrawer();
    }
}
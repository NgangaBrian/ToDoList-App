package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
        DrawerLayout drawerLayout;
        NavigationView navigationView;
        ActionBarDrawerToggle drawerToggle;

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(drawerToggle.onOptionsItemSelected(item)){
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base);


        }

        protected void setUpNavigationDrawer() {
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.todolist:{
                            Intent intent = new Intent(BaseActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                        case R.id.notes: {
                            Intent a = new Intent(BaseActivity.this, Notes.class);
                            startActivity(a);
                            finish();
                            break;
                        }
                        case R.id.schedule:{
                            Intent b = new Intent(BaseActivity.this, Schedule.class);
                            startActivity(b);
                            finish();
                            break;}
                        case R.id.contact:{
                            Toast.makeText(BaseActivity.this, "Contact us via todolist@gmail.com", Toast.LENGTH_LONG).show();
                            break;}
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }

        @Override
        public void onBackPressed() {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                super.onBackPressed();
            }
        }
    }



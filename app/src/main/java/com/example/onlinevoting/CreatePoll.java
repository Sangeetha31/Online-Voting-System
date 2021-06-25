package com.example.onlinevoting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreatePoll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        getSupportActionBar().setTitle("Create Poll");
    }
}
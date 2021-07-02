package com.example.onlinevoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.logging.Handler;

public class firstPage extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        getSupportActionBar().hide();

         new android.os.Handler().postDelayed(()->{
             Intent intent=new Intent(firstPage.this,MainActivity.class);
             startActivity(intent);
                 },
                 5000);
    }
}
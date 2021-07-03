package com.example.onlinevoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_mainpage extends AppCompatActivity {
    CardView logout,createPoll,edit;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mainpage);
        getSupportActionBar().setTitle("Home");
        logout = findViewById(R.id.logout);
        createPoll=findViewById(R.id.create);
        edit=findViewById(R.id.edit);
        db= FirebaseFirestore.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").document("admin").update("loggedIn",false);
                Intent intent = new Intent(getApplicationContext(),Admin_Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        createPoll .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_mainpage.this, CreatePoll.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(admin_mainpage.this,edit_profile_admin.class);
                startActivity(intent);
            }
        });

    }
}

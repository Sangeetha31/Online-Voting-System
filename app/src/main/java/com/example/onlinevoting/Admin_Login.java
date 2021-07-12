package com.example.onlinevoting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Admin_Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText inputEmail;
    private EditText inputPassword;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().setTitle("Admin Login");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#185cab"));
        actionBar.setBackgroundDrawable(colorDrawable);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        signInBtn = findViewById(R.id.signInBtn);
        CollectionReference collectionReference = db.collection("users");
        DocumentReference documentReference = collectionReference.document("admin");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if((Boolean)documentSnapshot.get("loggedIn") == false){
                    signInBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!validateEmail() || !validatePassword()) {
                                return;
                            }
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.d("inputEmail:",inputEmail.getText().toString());
                                    if(documentSnapshot.get("email").toString().equals(inputEmail.getText().toString()) && documentSnapshot.get("password").toString().equals(inputPassword.getText().toString())){
                                        Toast.makeText(Admin_Login.this, "You have successfully logged in!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), admin_mainpage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(Admin_Login.this, "Please check your email id or password", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),Admin_Login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),admin_mainpage.class);
                    startActivity(intent);
                }
            }
        });
    }

    private Boolean validateEmail () {
        String name_str = inputEmail.getText().toString();
        if(name_str.isEmpty()  ){
            inputEmail.setError("It cannot be empty");
            return false;
        }
        else {
            inputEmail.setError(null);
            return  true;
        }

    }

    private Boolean validatePassword () {
        String name_str = inputPassword.getText().toString();
        if(name_str.isEmpty()  ){
            inputPassword.setError("It cannot be empty");
            return false;
        }
        else {
            inputPassword.setError(null);
            return  true;
        }

    }
}

package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
  TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }

        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);
        register = findViewById(R.id.onClickRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,register.class);
                startActivity(intent);
            }
        });

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("users");
                DocumentReference documentReference = collectionReference.document(inputMobile.getText().toString());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                Toast.makeText(login.this, "You need to register first. Please register to continue", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), register.class);
                                startActivity(intent);
                            } else {
                                if (inputMobile.getText().toString().trim().isEmpty()) {
                                    Toast.makeText(login.this, "Enter Mobile Number", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                progressBar.setVisibility(View.VISIBLE);
                                buttonGetOTP.setVisibility((View.INVISIBLE));

                                PhoneAuthProvider.verifyPhoneNumber(
                                        PhoneAuthOptions
                                                .newBuilder(FirebaseAuth.getInstance())
                                                .setActivity(login.this)
                                                .setPhoneNumber("+91" + inputMobile.getText().toString())
                                                .setTimeout(60L, TimeUnit.SECONDS)
                                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                    @Override
                                                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                                        progressBar.setVisibility(View.GONE);
                                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                                    }

                                                    @Override
                                                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                                        progressBar.setVisibility(View.GONE);
                                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onCodeSent(@NonNull @NotNull String verificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                        progressBar.setVisibility(View.GONE);
                                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                                        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                                        intent.putExtra("mobile", inputMobile.getText().toString());
                                                        intent.putExtra("verificationId", verificationId);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .build());
                                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                intent.putExtra("mobile", inputMobile.getText().toString());
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
}
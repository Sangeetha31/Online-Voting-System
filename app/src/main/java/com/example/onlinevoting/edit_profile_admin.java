package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

public class edit_profile_admin extends AppCompatActivity {
    String old_password, email;
    EditText username, password, confirm_new_password;
    Button update;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference = db.collection("users").document("admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin);
        getSupportActionBar().setTitle("Edit your details");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#185cab"));
        actionBar.setBackgroundDrawable(colorDrawable);

        username = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        update = findViewById(R.id.update);
        confirm_new_password = findViewById(R.id.confirm_new_password);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {

                            email = task.getResult().getString("email");
                            old_password = task.getResult().getString("password");

                            username.setText(email);
                        } else {
                            Toast.makeText(edit_profile_admin.this, "Admin profile does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateProfile() {
        String name = username.getText().toString();
        String upassword = password.getText().toString();
        String new_password_confirm = confirm_new_password.getText().toString();
        if(upassword.isEmpty()){
            password.setError("It cannot be empty");
        }
        else if(new_password_confirm.isEmpty()){
            confirm_new_password.setError("It cannot be empty");
        }
        else if (upassword.length() < 5) {
           password.setError("It should be atleast greater than 5 characters");
        } else if (upassword.equals(new_password_confirm) == false) {
            Toast.makeText(edit_profile_admin.this, "Passwords don't match", Toast.LENGTH_LONG).show();
        } else if (name.equals(email) && upassword.equals(old_password)) {
            Toast.makeText(edit_profile_admin.this, "Nothing to be updated", Toast.LENGTH_LONG).show();
        } else {
            final DocumentReference doc = db.collection("users").document("admin");
            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                public Void apply(@NonNull @NotNull Transaction transaction) throws FirebaseFirestoreException {
                    transaction.update(doc, "email", name);
                    transaction.update(doc, "password", upassword);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(edit_profile_admin.this, "Updated", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(edit_profile_admin.this, "Failed", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
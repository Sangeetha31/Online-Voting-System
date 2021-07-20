package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class edit_Profile extends AppCompatActivity {
    String name1, date_of_birth1, phone_number1, voters_id1, mobile,gender1;
    EditText name, date_of_birth, phone_number, voters_id;
    Spinner spinner_gender;
    String date = "";
    ArrayAdapter<String> myAdapter;
    Button update;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mobile = getIntent().getStringExtra("mobile");
        documentReference = db.collection("users").document(mobile);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_Profile.this, home.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        name = findViewById(R.id.inputname);
        phone_number = findViewById(R.id.inputPhoneNumber);
        date_of_birth = findViewById(R.id.inputdateofbirth);
        voters_id = findViewById(R.id.inputvotersId);
        update = findViewById(R.id.update);

         spinner_gender = (Spinner) findViewById(R.id.gender);
        myAdapter = new ArrayAdapter<String>(edit_Profile.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(myAdapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        edit_Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        date = day + "/" + month + "/" + year;
                        Calendar today = Calendar.getInstance();
                        int age = today.get(Calendar.YEAR) - year;
                        if (age < 18) {
                            date_of_birth.setError("Your age should be above 18");
                        } else {
                            date_of_birth.setText(date);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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

                            name1 = task.getResult().getString("Name");
                            phone_number1 = task.getResult().get("PhoneNumber").toString();
                            date_of_birth1 = task.getResult().getString("DateOfBirth");
                            voters_id1 = task.getResult().getString("VotersId");
                            gender1 = task.getResult().getString("Gender");


                            name.setText(name1);
                            phone_number.setText(phone_number1);
                            date_of_birth.setText(date_of_birth1);
                            voters_id.setText(voters_id1);



                        } else {
                            Toast.makeText(edit_Profile.this, "User profile does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateProfile() {
        String name2 = name.getText().toString();
        String phoneNumber = phone_number.getText().toString();
        String dateOfBirth = date_of_birth.getText().toString();
        String gender = spinner_gender.getSelectedItem().toString();
        if (!validateName() || !validatedob() || !validatphoneno() || !validatvotersid()) {
            return;
        }
        else  if (name2.equals(name1) && phoneNumber.equals(phone_number1) && dateOfBirth.equals(date_of_birth1) && gender.equals(gender1) ) {
            Toast.makeText(edit_Profile.this, "Nothing to be updated", Toast.LENGTH_LONG).show();
        }

            else {
            final DocumentReference doc = db.collection("users").document(mobile);
            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                public Void apply(@NonNull @NotNull Transaction transaction) throws FirebaseFirestoreException {
                    transaction.update(doc, "Name", name2);
                    transaction.update(doc, "PhoneNumber", phoneNumber);
                    transaction.update(doc,"DateOfBirth",dateOfBirth);
                    transaction.update(doc,"Gender",gender);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(edit_Profile.this, "Updated", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(edit_Profile.this, "Failed", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private Boolean validateName () {
        String name_str = name.getText().toString();
        if(name_str.isEmpty()  ){
            name.setError("It cannot be empty");
            return false;
        }
        else {
            name.setError(null);
            return  true;
        }

    }
    private Boolean validatedob () {
        String dob= date_of_birth.getText().toString();
        if(dob.isEmpty()  ){
            date_of_birth.setError("It cannot be empty");
            return false;
        }
        else {
            date_of_birth.setError(null);
            return  true;
        }

    }
    private Boolean validatphoneno () {
        String phone= phone_number.getText().toString();
        if(phone.isEmpty()){
            phone_number.setError("It cannot be empty");
            return false;
        }
        else if(phone.toString().length()!=10){
            phone_number.setError("It should be 10-digit phone number");
            return false;
        }
        else{
            phone_number.setError(null);
            return  true;
        }

    }
    private Boolean validatvotersid () {
        String votersId=voters_id.getText().toString();
        char[] charArray = votersId.toCharArray();
        for (int i = 0; i < 3; i++) {
            char ch = charArray[i];
            if (!(ch >= 'A' && ch <= 'Z')) {
                voters_id.setError("It is not a valid voter's id number");
                return false;
            }
        }
        for (int i = 3; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= '0' && ch <= '9')) {
                voters_id.setError("It is not a valid voter's id number");
                return false;
            }
        }
        if(votersId.isEmpty() ){
            voters_id.setError("It cannot be empty");
            return false;
        }
        else if(votersId.length()!=10){
            voters_id.setError("It should be 10-digits");
            return false;
        }
        else{
            voters_id.setError(null);
            return  true;
        }

    }
}

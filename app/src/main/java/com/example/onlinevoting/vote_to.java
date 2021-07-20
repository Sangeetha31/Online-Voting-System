package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class vote_to extends AppCompatActivity {

    private static final String TAG = "User Id";
    RecyclerView recyclerView;
    ArrayList<candidate> candidateArrayList;
    adapterForcandidate adapterForcandidate;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_to);

        mobile = getIntent().getStringExtra("mobile");
        getSupportActionBar().setTitle("Edit your details");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#185cab"));
        actionBar.setBackgroundDrawable(colorDrawable);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Candidate's list");
        progressDialog.show();
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        candidateArrayList = new ArrayList<candidate>();
        adapterForcandidate = new adapterForcandidate(vote_to.this, candidateArrayList,  new adapterForcandidate.ItemClickListener(){
            @Override
            public void onItemClick(candidate candidates) {
                Vote(candidates.getName(),candidates.getVotes());
            }
        });
        recyclerView.setAdapter(adapterForcandidate);

        EventChangeListener();
    }


    private void Vote(String Name, int Votes){
            db.collection("users").document(mobile).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists() && !(boolean)task.getResult().get("Voted")){
                        db.collection("users/admin/candidate").document(Name).update("Votes", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                String uid = db.collection("users").getId();
                                db.collection("users").document(mobile).update("Voted",true);
                                Toast.makeText(vote_to.this,"You voted for " + Name,Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(vote_to.this,"Try again later",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(vote_to.this,"You have already voted!",Toast.LENGTH_LONG).show();
                    }
                }
            });

           // Log.d(TAG,uid);

    }

    private void EventChangeListener() {
        db.collection("users/admin/candidate").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                      if(error != null){
                          if(progressDialog.isShowing()){
                              progressDialog.dismiss();
                          }
                          Log.e("Firestore error",error.getMessage());
                          return;
                      }
                       for(DocumentChange dc: value.getDocumentChanges()){
                           if(dc.getType() == DocumentChange.Type.ADDED){
                               candidateArrayList.add(dc.getDocument().toObject(candidate.class));
                           }
                           adapterForcandidate.notifyDataSetChanged();
                           if(progressDialog.isShowing()){
                               progressDialog.dismiss();
                           }
                       }
                    }
                });
    }

}
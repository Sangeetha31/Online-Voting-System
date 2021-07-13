package com.example.onlinevoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class vote_to extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<candidate> candidateArrayList;
    adapterForcandidate adapterForcandidate;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_to);
        getSupportActionBar().setTitle("Vote");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Candidate's list");
        progressDialog.show();


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        candidateArrayList = new ArrayList<candidate>();
        adapterForcandidate = new adapterForcandidate(vote_to.this,candidateArrayList);
        recyclerView.setAdapter(adapterForcandidate);

        EventChangeListener();
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
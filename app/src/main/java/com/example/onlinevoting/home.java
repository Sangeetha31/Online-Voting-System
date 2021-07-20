package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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


public class home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView imageView,clickLogo;
    LinearLayout about,editProfile,logout,vote;

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
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);

        imageView = findViewById(R.id.ClickMenu);
        clickLogo = findViewById(R.id.Clicklogo);
        about=findViewById(R.id.about);
        editProfile=findViewById(R.id.editProfile);
        vote=findViewById(R.id.vote);
        logout=findViewById(R.id.logout);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }

        });
        clickLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(home.this,about_page.class);
                startActivity(intent1);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(home.this,edit_Profile.class);
                intent2.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent2);
            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(home.this,vote_to.class);
                intent3.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent3);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(home.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(home.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        mobile = getIntent().getStringExtra("mobile");
        getSupportActionBar().setTitle("Edit your details");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Candidate's list");
        progressDialog.show();
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        candidateArrayList = new ArrayList<candidate>();
        adapterForcandidate = new adapterForcandidate(home.this, candidateArrayList,  new adapterForcandidate.ItemClickListener(){
            @Override
            public void onItemClick(candidate candidates) {
                Vote(candidates.getName(),candidates.getVotes());
            }
        });
        recyclerView.setAdapter(adapterForcandidate);

        EventChangeListener();

    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
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
                            Toast.makeText(home.this,"You voted for " + Name,Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(home.this,"Try again later",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(home.this,"You have already voted!",Toast.LENGTH_LONG).show();
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
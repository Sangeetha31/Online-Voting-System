package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView imageView, clickLogo;
    LinearLayout about, editProfile, logout;
    ListView listView;
    FirebaseFirestore db;
    private static final String TAG = home.class.getName();
    private StorageReference mStoragereference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // imageView = findViewById(R.id.ClickMenu);
        clickLogo = findViewById(R.id.Clicklogo);
        about = findViewById(R.id.about);
        editProfile = findViewById(R.id.editProfile);
        logout = findViewById(R.id.logout);
        listView = findViewById(R.id.ListView);
        db = FirebaseFirestore.getInstance();
        mStoragereference = FirebaseStorage.getInstance().getReference().child("Candidates/");

        //Make an array of data
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> despList = new ArrayList<>();


        db.collection("users/admin/candidate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {
                        //arrayList.add(new Party(ds.getString("Name"),ds.getString("Description")));
                        nameList.add(ds.getString("Name"));
                        despList.add(ds.getString("Description"));
                    }
                    //Log.d(TAG,arrayList.get(0).Name + arrayList.get(0).Desp);
                } else {
                    Log.d(TAG, "Did not get any values");
                }

            }
        });

        //Create a custom adapter
        // PartyAdapter partyAdapter = new PartyAdapter(this,R.layout.listview,arrayList);

        ArrayAdapter<String> partyAdapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.txtName, nameList);

        listView.setAdapter(partyAdapter);


      /*  imageView.setOnClickListener(new View.OnClickListener() {
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
                startActivity(intent2);
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
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }*/
    }
}

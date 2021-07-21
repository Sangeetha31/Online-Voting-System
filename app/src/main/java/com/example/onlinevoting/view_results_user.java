package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class view_results_user extends AppCompatActivity {
    String mobile;

    BarChart barChart;
    TextView message;
    FirebaseFirestore db;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelNames;
    ArrayList<namesVotes> namesVotes = new ArrayList<>();
    private static ArrayList<Long> votes = new ArrayList<>();
    private  static ArrayList<String> names_of_candidate = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        barEntryArrayList.clear();
        labelNames.clear();
        namesVotes.clear();
        votes.clear();
        names_of_candidate.clear();
        barChart.invalidate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results_user);

        Toolbar toolbar = findViewById(R.id.app_bar);
        mobile = getIntent().getStringExtra("mobile");



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_results_user.this, home.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });


        barChart = findViewById(R.id.BarChart);
        message = findViewById(R.id.annoucement);
        db=FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("users");
        DocumentReference documentReference = collectionReference.document("admin");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if ((Boolean) documentSnapshot.get("released") == false) {
                    barChart.setNoDataText("The results have not yet released. Please wait");
                    barChart.invalidate();
                } else {
                    getBarChart();
                }
            }
        });
}
    private void getBarChart() {
        db= FirebaseFirestore.getInstance();
        db.collection("users/admin/candidate").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Long Votes = (Long) queryDocumentSnapshot.get("Votes");
                                votes.add(Votes);
                                String name = (String) queryDocumentSnapshot.get("Name");
                                names_of_candidate.add(name);
                            }
                            barEntryArrayList = new ArrayList<>();
                            labelNames = new ArrayList<>();

                            for(int i=0;i < votes.size();i++)
                                namesVotes.add(new namesVotes(names_of_candidate.get(i),votes.get(i)));
                            for(int i=0;i < namesVotes.size(); i++){
                                String name = namesVotes.get(i).getName();
                                Long votes = namesVotes.get(i).getVotes();
                                barEntryArrayList.add(new BarEntry(i,votes));
                                labelNames.add(name);
                            }
                            BarDataSet barDataSet = new BarDataSet(barEntryArrayList,"Voting Results");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barChart.setDescription(null);
                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                            xAxis.setTextSize(15);
                            xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(labelNames.size());
                            xAxis.setLabelRotationAngle(270);
                            barChart.animateY(2000);
                            barChart.invalidate();
                            Long max = namesVotes.get(0).votes;
                            String winner = namesVotes.get(0).name;
                            for(int i=0;i < namesVotes.size();i++){
                                if( namesVotes.get(i).votes > max) {
                                    max = namesVotes.get(i).votes;
                                    winner = namesVotes.get(i).name;
                                }
                            }

                            message.setText(winner + " is winner");
                        } else {
                            Toast.makeText(getApplicationContext(), "Not able to fetch data", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
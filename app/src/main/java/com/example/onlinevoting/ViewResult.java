package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.onlinevoting.R.layout.activity_view_result;
import static com.github.mikephil.charting.utils.ColorTemplate.*;

public class ViewResult extends AppCompatActivity {

    String name;
    BarChart barChart;
    BarData barData;
    ArrayList<BarEntry> barEntries;
    BarDataSet barDataSet;
    //HorizontalBarChart mChart;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference voteReference = db.document("users/admin/candidate");
//    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_view_result);
        getSupportActionBar().setTitle("Result");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#185cab"));
        actionBar.setBackgroundDrawable(colorDrawable);
//        mChart = findViewById(R.id.HBChart);
//        SetData(12, 50);
        barChart = findViewById(R.id.BarChart);
        SetData();

        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
    }

    private void SetData(){
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2f,5));
        barEntries.add(new BarEntry(4f,1));
        barEntries.add(new BarEntry(5f,2));
        barEntries.add(new BarEntry(6f,3));
        barEntries.add(new BarEntry(7f,4));
        barEntries.add(new BarEntry(8f,5));
        barEntries.add(new BarEntry(6f,6));
        /*voteReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            name = (documentSnapshot.getString("NAME"));
                            Log.e(TAG, name);
                        }
                        else{
                            Toast.makeText(ViewResult.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });*/
        /*barEntries = new ArrayList<>();
        float barWidth = 9f;
        float spaceForBar = 10f;
        for(int i =0; i<count; i++){
            float val = (float)(Math.random()*range);
            barEntries.add(new BarEntry(i*spaceForBar, val));
        }
        barDataSet= new BarDataSet(barEntries, "Vote Results");
        barData = new BarData(barDataSet);
        barData.setBarWidth(barWidth);
        mChart.setData(barData);*/
    }
}
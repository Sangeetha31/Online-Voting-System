package com.example.onlinevoting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class adapterForcandidate extends RecyclerView.Adapter<adapterForcandidate.MyViewHolder> {

    Context context;
    ArrayList<candidate> candidateArrayList;
    private ItemClickListener mItemClickListener;

    public adapterForcandidate(Context context, ArrayList<candidate> candidateArrayList) {
        this.context = context;
        this.candidateArrayList = candidateArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public adapterForcandidate.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull adapterForcandidate.MyViewHolder holder, int position) {
      candidate candidate = candidateArrayList.get(position);
      holder.name.setText(candidate.Name);
      holder.desp.setText(candidate.Description);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
    }

    @Override
    public int getItemCount() {
        return candidateArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,desp;
        Button voteBtn;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            desp=itemView.findViewById(R.id.desp);
        }
    }
}


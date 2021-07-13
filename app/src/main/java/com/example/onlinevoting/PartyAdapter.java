package com.example.onlinevoting;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PartyAdapter extends ArrayAdapter<Party> {
    private Context mContext;
    private int mResource;

    public PartyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Party> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /*@NonNull
    @Override
    public View getView(int position,  View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);


        TextView txtView = convertView.findViewById(R.id.txtName);

        TextView desp = convertView.findViewById(R.id.desp);

        txtView.setText(getItem(position).getName());

        desp.setText(getItem(position).getDesp());

        return convertView;
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);


        TextView txtView = convertView.findViewById(R.id.txtName);

        TextView desp = convertView.findViewById(R.id.desp);

        txtView.setText(getItem(position).getName());

        desp.setText(getItem(position).getDesp());

        return super.getView(position, convertView, parent);
    }
}

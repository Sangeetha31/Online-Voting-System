package com.example.onlinevoting;


import com.google.firebase.storage.StorageReference;

public class Party {
   // StorageReference symbol;
    String Name;
    String Desp;

    public Party(String name, String desp) {
        Name = name;
        Desp = desp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesp() {
        return Desp;
    }

    public void setDesp(String desp) {
        Desp = desp;
    }
}

package com.example.onlinevoting;

public class candidate {

    String Name,Description;

    public candidate(){}

    public candidate(String name, String description) {
        this.Name = name;
        this.Description = description;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setName(String name) {
        Name = name;
    }
}

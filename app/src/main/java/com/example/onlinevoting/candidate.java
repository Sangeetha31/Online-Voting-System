package com.example.onlinevoting;

public class candidate {

    String Name,Description,imageUrl;
    int Votes;


    public candidate(){}

    public candidate(String name, String description, String imageUrl, int votes, boolean voted) {
        Name = name;
        Description = description;
        this.imageUrl = imageUrl;
        Votes = votes;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getVotes() {
        return Votes;
    }

    public void setVotes(int votes) {
        Votes = votes;
    }
}

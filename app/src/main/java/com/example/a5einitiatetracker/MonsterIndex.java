package com.example.a5einitiatetracker;

import com.google.gson.annotations.SerializedName;

public class MonsterIndex {

    @SerializedName("index")
    private String index;

    @SerializedName("name")
    private String name;

    public MonsterIndex(String index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

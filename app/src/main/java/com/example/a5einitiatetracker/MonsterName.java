package com.example.a5einitiatetracker;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MonsterName {

    //region VARIABLES
    @SerializedName("name")
    private String name;

    @SerializedName("index")
    private String index;

    public MonsterName(String name, String index) {
        this.name = name;
        this.index = index;
    }
    //endregion

    //region PUBLIC FUNCTIONS/METHODS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    //endregion
}

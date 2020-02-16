package com.example.a5einitiatetracker.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonsterIndex {

    //region VARIABLES
    @SerializedName("results")
    private List<MonsterName> results;

    //endregion

    //region PUBLIC FUNCTIONS/METHODS

    public MonsterIndex(List<MonsterName> results) {
        this.results = results;
    }

    public List<MonsterName> getResults() {
        return results;
    }

    public void setResults(List<MonsterName> results) {
        this.results = results;
    }

    //endregion
}

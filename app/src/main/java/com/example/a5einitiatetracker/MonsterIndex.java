package com.example.a5einitiatetracker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonsterIndex {
    @SerializedName("results")
    private List<MonsterName> results;

    public MonsterIndex(List<MonsterName> results) {
        this.results = results;
    }

    public List<MonsterName> getResults() {
        return results;
    }

    public void setResults(List<MonsterName> results) {
        this.results = results;
    }
}

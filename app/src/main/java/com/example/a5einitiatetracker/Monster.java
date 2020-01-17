//Parser class to handle data returned by the rest API.

package com.example.a5einitiatetracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Monster {
        @Expose
        @SerializedName("hit_points")
        private Integer hitPoints;
        @Expose
        @SerializedName("dexterity")
        private Integer dexterity;
        @Expose
        @SerializedName("name")
        private String name;

    public Integer getHit_points() {
        return hitPoints;
    }

    public void setHit_points(Integer hit_points) {
        this.hitPoints = hit_points;
    }

    public Integer getDexterity() {
        return dexterity;
    }

    public void setDexterity(Integer dexterity) {
        this.dexterity = dexterity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

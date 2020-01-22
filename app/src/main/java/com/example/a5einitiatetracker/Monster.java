//Parser class to handle data returned by the rest API.

package com.example.a5einitiatetracker;

import com.google.gson.annotations.SerializedName;

public class Monster {

    //region VARIABLES
    @SerializedName("name")
    private String name;
    @SerializedName("hit_points")
    private Integer hp;
    @SerializedName("dexterity")
    private Integer dex;

    public Monster(String name, Integer hp, Integer dex) {
        this.name = name;
        this.hp = hp;
        this.dex = dex;
    }
    //endregion

    //region PUBLIC FUNCTIONS/METHODS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getDex() {
        return dex;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }
    //endregion
}

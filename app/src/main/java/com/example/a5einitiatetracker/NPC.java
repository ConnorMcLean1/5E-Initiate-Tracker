package com.example.a5einitiatetracker;

import androidx.annotation.NonNull;

import java.util.Random;

class NPC extends Combatant implements Comparable<Combatant> {

    //region VARIABLES
    private final int ADVANTAGE = 1; // roll with advantage
    private final int DISADVANTAGE = -1; // roll with disadvantage
    private int health;
    private Random roller = new Random();
    //endregion

    //region PUBLIC FUNCTIONS/METHODS
    public NPC(){
        health = 0;
        super.initiative = 0;
        super.initiativeModifier = 0;
        super.status = combatantStates.ALIVE;
    }

    public NPC(int initiativeModifier, combatantStates status, int health, String name, int adv) {
        this.status = status;
        this.health = health;
        this.initiativeModifier = initiativeModifier;
        this.name = name;
        this.initiative = rollInitiative(adv);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int compareTo(Combatant o) {
        return this.getInitiative().compareTo(o.getInitiative());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + String.format("Health: %d\n\n", health);
    }

    //endregion

    //region PRIVATE FUNCTIONS/METHODS
    protected static int dexToMod(int dex){
        Double mod = Math.floor(((dex-10)/2));
        return mod.intValue();
    }

    //Method to roll initiative. Note for input 0 = no extra roll, 1 = advantage, -1 = disadvantage
    private int rollInitiative(int adv){
        int temp = roller.nextInt(21);

        if(adv == ADVANTAGE){
            int temp2 = roller.nextInt(21);
            if(temp > temp2)
                return temp  + initiativeModifier;
            else
                return temp2  + initiativeModifier;
        }
        else if (adv == DISADVANTAGE){
                int temp2 = roller.nextInt(21);
                if(temp < temp2)
                    return temp  + initiativeModifier;
                else
                    return temp2  + initiativeModifier;
        }
        else
            return temp + initiativeModifier; // no roll modifier
    }

    //endregion

}

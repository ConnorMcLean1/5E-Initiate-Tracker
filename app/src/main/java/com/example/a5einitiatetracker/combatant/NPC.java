package com.example.a5einitiatetracker.combatant;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;

public class NPC extends Combatant implements Comparable<Combatant> {

    //region VARIABLES
    private final int ADVANTAGE = 1; // roll with advantage
    private final int DISADVANTAGE = -1; // roll with disadvantage
    private final int maxHealth;
    private final int MINDEATHSAVESUCCESS = 10;
    private int health;
    public enum deathSaveResult {SUCCESS, FAILURE, CRITICALSUCCESS, NONE}
    public enum deathSaveState {UNSTABLE, DEAD, STABLE, ALIVE}
    private deathSaveResult[] deathSaves;
    private Random roller = new Random();
    //endregion

    //region PUBLIC FUNCTIONS/METHODS
    public NPC(){
        health = 0;
        maxHealth = 0;
        deathSaves = new deathSaveResult[]{deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE};
        super.initiative = 0;
        super.initiativeModifier = 0;
        super.status = combatantStates.ALIVE;
    }

    public NPC(int initiativeModifier, combatantStates status, int health, String name, int adv) {
        this.status = status;
        this.health = health;
        this.maxHealth = health;
        this.initiativeModifier = initiativeModifier;
        this.name = name;
        this.initiative = rollInitiative(adv);
        deathSaves = new deathSaveResult[]{deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE};
    }

    public deathSaveResult[] getDeathSaves() {
        return deathSaves;
    }

    public void setDeathSaves(deathSaveResult[] deathSaves) {
        this.deathSaves = deathSaves;
    }

    public int getMaxHealth() {return maxHealth;}

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

    public static int dexToMod(int dex){
        Double mod = Math.floor(((dex-10)/2));
        return mod.intValue();
    }

    //endregion

    //region PRIVATE FUNCTIONS/METHODS

    //Method to roll initiative. Note for input 0 = no extra roll, 1 = advantage, -1 = disadvantage
    private int rollInitiative(int adv){
        int temp = roller.nextInt(20) + 1;

        if(adv == ADVANTAGE){
            int temp2 = roller.nextInt(20) + 1;
            if(temp > temp2)
                return temp  + initiativeModifier;
            else
                return temp2  + initiativeModifier;
        }
        else if (adv == DISADVANTAGE){
            int temp2 = roller.nextInt(20) + 1;
            if(temp < temp2)
                return temp  + initiativeModifier;
            else
                return temp2  + initiativeModifier;
        }
        else
            return temp + initiativeModifier;
    }

    //Rolls a death saving throw and returns the rolled value between 1 and 20
    public void rollDeathSave(int adv, int bonus){
        int temp = roller.nextInt(20) + 1;
        int roll;
        deathSaveResult result;

        if(adv == ADVANTAGE){
            int temp2 = roller.nextInt(20) + 1;
            if(temp == 20 || temp2 == 20) {
                setNextDeathSave(deathSaveResult.CRITICALSUCCESS);
                return;
            }
            else if(temp > temp2)
                roll = temp + bonus;
            else
                roll = temp2 + bonus;
        }
        else if (adv == DISADVANTAGE){
            int temp2 = roller.nextInt(20) + 1;
            if(temp == 20 && temp2 == 20) {
                setNextDeathSave(deathSaveResult.CRITICALSUCCESS);
                return;
            }
            else if(temp < temp2)
                roll = temp + bonus;
            else
                roll = temp2 + bonus;
        }
        else
            roll = temp + bonus;

        if(roll < MINDEATHSAVESUCCESS)
            setNextDeathSave(deathSaveResult.FAILURE);
        else
            setNextDeathSave(deathSaveResult.SUCCESS);
    }

    public deathSaveState checkDeathSaves(){
        int successes = 0, failures = 0;
        deathSaveResult temp;
        for(int i = 0; i < deathSaves.length; i++){
            temp = deathSaves[i];
            switch (temp){
                case SUCCESS:
                    successes++;
                    break;
                case FAILURE:
                    failures++;
                    break;
                case CRITICALSUCCESS:
                    return deathSaveState.ALIVE;
                case NONE:
                    i = deathSaves.length; //break the loop
                    break;
            }
        }

        if(successes < 3 && failures < 3)
            return deathSaveState.UNSTABLE;
        else if(successes >= 3)
            return deathSaveState.STABLE;
        else
            return deathSaveState.DEAD;
    }

    public void setNextDeathSave(deathSaveResult result){
        int i = 0;
        while(i < deathSaves.length && deathSaves[i] != deathSaveResult.NONE)
            i++;
        deathSaves[i] = result;
    }

    public void resetDeathSaves(){
        deathSaves = new deathSaveResult[]{deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE, deathSaveResult.NONE};
    }

    public boolean damageNpc(int damage, Context context){
        health -= damage;
        if(health < 0){
            int overkill = Math.abs(health);
            health = 0;
            if(overkill >= maxHealth){ //If the current combatant would be outright killed by the damage
                Toast.makeText(context, "The combatant has been instantly killed by taking massive damage.", Toast.LENGTH_SHORT).show();
                status = Combatant.combatantStates.DEAD;
                Log.d("damageHpClick", "The combatant: " + name + " is killed by RAW.");
            }
            else if(status == Combatant.combatantStates.UNSTABLE){
                setNextDeathSave(NPC.deathSaveResult.FAILURE);
                return true;
            }
            else if(status != Combatant.combatantStates.DEAD){
                Toast.makeText(context, "The combatant has been reduced to 0 HP and is now unstable.", Toast.LENGTH_SHORT).show();
                status = Combatant.combatantStates.UNSTABLE;
                Log.d("damageHpClick", "The combatant: " + name + " is at 0 HP");
            }
        }
        else if(health == 0){
            Toast.makeText(context, "The combatant has been reduced to 0 HP and is now unstable.", Toast.LENGTH_SHORT).show();
            status = Combatant.combatantStates.UNSTABLE;
            Log.d("damageHpClick", "The combatant: " + name + " is at 0 HP");
        }
        return false;
    }



    //endregion

}
package com.example.a5einitiatetracker.combatant;

import androidx.annotation.NonNull;

public class Combatant {

    //region VARIABLES
    protected Integer initiative;
    protected int initiativeModifier;
    public enum combatantStates {ALIVE, DEAD, UNCONSCIOUS, UNSTABLE}
    protected combatantStates status;
    protected String name;
    //endregion

    //region PUBLIC FUNCTIONS/METHODS

    // default constructor
    public Combatant(){
        initiative = 0;
        initiativeModifier = 0;
        status = combatantStates.ALIVE;
    }

    // constructor with arguments
    public Combatant(int initiative, int initiativeModifier, combatantStates status, String name){
        this.initiative = initiative;
        this.initiativeModifier = initiativeModifier;
        this.status = status;
        this.name = name;
    }

    public Integer getInitiative() {
        return initiative;
    }

    public int getInitiativeModifier() {
        return initiativeModifier;
    }

    public combatantStates getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public combatantStates getCombatState() { return status; }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setStatus(combatantStates status) {
        this.status = status;
    }

    public void setInitiativeModifier(int initiativeModifier) {
        this.initiativeModifier = initiativeModifier;
    }

    public void setCombatState(combatantStates status) {
        this.status = status;
    }


    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Name: %s\nInitiative: %d\n", name, initiative);
    }

    //endregion

}



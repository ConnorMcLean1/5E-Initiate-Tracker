package com.example.a5einitiatetracker;

class Combatant {
    protected int initiative;
    protected int initiativeModifier;

    public Combatant(){
        initiative = 0;
        initiativeModifier = 0;
    }
    public Combatant(int initiative, int initiativeModifier){
        this.initiative = initiative;
        this.initiativeModifier = initiativeModifier;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getInitiativeModifier() {
        return initiativeModifier;
    }

    public void setInitiativeModifier(int initiativeModifier) {
        this.initiativeModifier = initiativeModifier;
    }
}



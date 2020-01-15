package com.example.a5einitiatetracker;

class Combatant {
    protected int initiative;
    protected int initiativeModifier;
    protected enum combatantStates {ALIVE, DEAD, UNCONSCIOUS, UNSTABLE}
    protected combatantStates status;

    public Combatant(){
        initiative = 0;
        initiativeModifier = 0;
        status = combatantStates.ALIVE;
    }
    public Combatant(int initiative, int initiativeModifier, combatantStates status){
        this.initiative = initiative;
        this.initiativeModifier = initiativeModifier;
        this.status = status;
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

    public combatantStates getStatus() {
        return status;
    }

    public void setStatus(combatantStates status) {
        this.status = status;
    }

    public void setInitiativeModifier(int initiativeModifier) {
        this.initiativeModifier = initiativeModifier;
    }

}



package com.example.a5einitiatetracker;

class Combatant {

    //region VARIABLES
    protected int initiative;
    protected Integer initiativeModifier;
    protected enum combatantStates {ALIVE, DEAD, UNCONSCIOUS, UNSTABLE}
    protected combatantStates status;
    //endregion

    //region PUBLIC FUNCTIONS/METHODS

    // default constructor
    public Combatant(){
        initiative = 0;
        initiativeModifier = 0;
        status = combatantStates.ALIVE;
    }

    // constructor with arguments
    public Combatant(int initiative, int initiativeModifier, combatantStates status){
        this.initiative = initiative;
        this.initiativeModifier = initiativeModifier;
        this.status = status;
    }

    public int getInitiative() {
        return initiative;
    }

    public Integer getInitiativeModifier() {
        return initiativeModifier;
    }

    public combatantStates getStatus() {
        return status;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setStatus(combatantStates status) {
        this.status = status;
    }

    public void setInitiativeModifier(int initiativeModifier) {
        this.initiativeModifier = initiativeModifier;
    }
    //endregion

}



package com.dnd5e.combattracker.combatant;

public class Player extends Combatant {

    //region PUBLIC FUNCTIONS/METHODS
    public Player() {
        super.initiative = 0;
        super.initiativeModifier = 0;
        super.status = combatantStates.ALIVE;
    }

    public Player(int initiative, int initiativeModifier, combatantStates status, String name) {
        super(initiative, initiativeModifier, status, name);
    }

    public Player(int initiative, String name) {
        this.initiative = initiative;
        this.name = name;
        this.status = combatantStates.ALIVE;
    }
    //endregion

}

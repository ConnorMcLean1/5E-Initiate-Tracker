package com.example.a5einitiatetracker;

class Player extends Combatant {
    public Player() {
        super.initiative = 0;
        super.initiativeModifier = 0;
        super.status = combatantStates.ALIVE;
    }

    public Player(int initiative, int initiativeModifier, combatantStates status) {
        super(initiative, initiativeModifier, status);
    }


}

package com.example.a5einitiatetracker;

class Player extends Combatant {
    public Player() {
        super.initiative = 0;
        super.initiativeModifier = 0;
    }

    public Player(int initiative, int initiativeModifier) {
        super(initiative, initiativeModifier);
    }


}

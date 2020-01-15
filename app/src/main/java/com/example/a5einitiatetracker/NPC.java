package com.example.a5einitiatetracker;

class NPC extends Combatant {
    protected int health;

    public NPC(){
        health = 0;
        super.initiative = 0;
        super.initiativeModifier = 0;
    }

    public NPC(int initiative, int initiativeModifier, int health) {
        super(initiative, initiativeModifier);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}

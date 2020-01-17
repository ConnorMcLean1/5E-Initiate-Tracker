package com.example.a5einitiatetracker;


import java.util.Random;

class NPC extends Combatant {
    private int health;
    private Random roller = new Random();

    public NPC(){
        health = 0;
        super.initiative = 0;
        super.initiativeModifier = 0;
        super.status = combatantStates.ALIVE;
    }

    public NPC(int initiative, int initiativeModifier, combatantStates status, int health) {
        super(initiative, initiativeModifier, status);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    //Method to roll intiative. Note for input 0 = no extra roll, 1 = advantage, -1 = disadvantage
    private int rollInitiative(int adv){
        int temp = roller.nextInt(21);
        if(adv == 1){
            int temp2 = roller.nextInt(21);
            if(temp > temp2)
                return temp  + initiativeModifier;
            else
                return temp2  + initiativeModifier;
        }
        else if (adv == -1){
                int temp2 = roller.nextInt(21);
                if(temp < temp2)
                    return temp  + initiativeModifier;
                else
                    return temp2  + initiativeModifier;
        }
        else
            return temp + initiativeModifier;
    }
}

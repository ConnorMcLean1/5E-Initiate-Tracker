package com.example.a5einitiatetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.ListIterator;

public class CombatActivity extends AppCompatActivity {
    ListIterator<Combatant> iterator;
    List<Combatant> combatantList;
    Combatant currCombatant;
    NPC npc;
    Player pc;
    Boolean combatComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iterator = combatantList.listIterator();
        combatComplete = false;

        //Button to go to the previous combatant in initiative
        Button nextButton = findViewById(R.id.);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        //Button to go to the next combatant in initiative
        Button previousButton = findViewById(R.id.);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(iterator.hasPrevious())
                    iterator.previous();
                else
                    iterator.
            }
        });

        //Button to change the health of the currently selected monster if they heal or take damage
        Button changeHpButton = findViewById(R.id.);
        changeHpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        //Button to change the combatant states (ALIVE, DEAD, STABLE, UNSTABLE)
        Button changeStateButton = findViewById(R.id.);
        changeStateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        //Button to roll death saving throws for the currently selected combatant
        Button rollDeathSaveButton = findViewById(R.id.);
        rollDeathSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NPC tempNPC = null;
                if(checkForDeathSave(tempNPC)){
                    //TODO need to have the combatant roll a death saving throw and return the result
                }
                else{
                    //TODO need to have a toast to let the DM know that the combatant is not in the unstable state, and possibly a popup to set their state to that
                }
            }
        });

        //Button to change the combatant states (ALIVE, DEAD, STABLE, UNSTABLE)
        Button endCombatButton = findViewById(R.id.);
        endCombatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                combatComplete = true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkForDeathSave(NPC npc){
        return (npc.status == Combatant.combatantStates.UNSTABLE);
    }
}

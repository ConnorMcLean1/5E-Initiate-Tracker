package com.example.a5einitiatetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.NPC;
import com.example.a5einitiatetracker.combatant.Player;

import java.util.List;
import java.util.ListIterator;

public class CombatActivity extends AppCompatActivity {
    ListIterator<Combatant> iterator;
    List<Combatant> combatantList;
    Combatant currCombatant;
    NPC npc;
    Player pc;
    Boolean combatComplete;
    int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iterator = combatantList.listIterator();
        combatComplete = false;

        //Button to go to the previous combatant in initiative
        Button nextButton = findViewById(R.id.);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count = 0; //Counter variable to prevent infinite looping if the combat only contains dead characters

                do { //Get the next combatant, skipping over dead ones
                    if (iterator.hasNext()) { //Check if there is another combatant in the list. If yes, grab it out
                        currCombatant = iterator.next();
                    }
                    else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                        iterator = combatantList.listIterator(0);
                        count++;
                        currCombatant = iterator.next();
                    }
                } while(currCombatant.getCombatState() != Combatant.combatantStates.DEAD && count < 2);

                if(count > 1){
                    //TODO The combat only contains dead combatants, and the loop would have gone infinitely. Need to add handling
                }

                if(currCombatant instanceof Player){ //Check if the current combatant is a player or not, and cast it appropriately
                    pc = (Player) currCombatant;
                }
                else{
                    npc = (NPC) currCombatant;
                }

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
        return (npc.getStatus() == Combatant.combatantStates.UNSTABLE);
    }

}

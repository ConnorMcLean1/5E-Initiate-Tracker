package com.example.a5einitiatetracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.NPC;
import com.example.a5einitiatetracker.combatant.Player;

import java.util.List;
import java.util.ListIterator;

public class CombatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ListIterator<Combatant> iterator;
    List<Combatant> combatantList;
    Combatant currCombatant;
    NPC npc;
    Player pc;
    Boolean combatComplete, isPlayer;
    int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iterator = combatantList.listIterator();
        combatComplete = false;

        //Button to go to the previous combatant in initiative
        Button nextButton = findViewById(R.id.btnNext);
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
                } while(currCombatant.getCombatState() == Combatant.combatantStates.DEAD && count < 2);

                if(count > 1){ //If the count goes over 1 the do/while likely would have gone on infinitely. The combat should end at this point, as there is nothing left to do
                    Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant or change their state to continue.", Toast.LENGTH_SHORT).show();
                    Log.d("MAIN_LOOP_TEST", "Next Button. No non-dead combatants. Loop would be infinite");
                }

                if(currCombatant instanceof Player){ //Check if the current combatant is a player or not, and cast it appropriately
                    pc = (Player) currCombatant;
                    isPlayer = true;
                    Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + currCombatant.getName() + " is a PC.");
                }
                else{
                    npc = (NPC) currCombatant;
                    isPlayer = false;
                    Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + currCombatant.getName() + " is a NPC.");
                }

            }
        });

        //Button to go to the next combatant in initiative
        Button previousButton = findViewById(R.id.btnPrev);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count = 0; //Counter variable to prevent infinite looping if the combat only contains dead characters

                do { //Get the next combatant, skipping over dead ones
                    if (iterator.hasPrevious()) { //Check if there is another combatant in the list. If yes, grab it out
                        currCombatant = iterator.previous();
                    } else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                        iterator = combatantList.listIterator(combatantList.size()-1);
                        count++;
                        currCombatant = iterator.previous();
                    }
                } while (currCombatant.getCombatState() == Combatant.combatantStates.DEAD && count < 2);

                if (count > 1) { //If the count goes over 1 the do/while likely would have gone on infinitely. The combat should end at this point, as there is nothing left to do
                    Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant or change their state to continue.", Toast.LENGTH_SHORT).show();
                    Log.d("MAIN_LOOP_TEST", "Previous Button. No non-dead combatants. Loop would be infinite");
                }

                if (currCombatant instanceof Player) { //Check if the current combatant is a player or not, and cast it appropriately
                    pc = (Player) currCombatant;
                    isPlayer = true;
                    Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + currCombatant.getName() + " is a PC.");
                }
                else {
                    npc = (NPC) currCombatant;
                    isPlayer = false;
                    Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + currCombatant.getName() + " is a NPC.");
                }
            }
        });

        //Button to change the health of the currently selected monster if they heal or take damage
        ImageButton changeHpButton = findViewById(R.id.btnHp);
        changeHpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int currCombatantHp;
                if(isPlayer){
                    Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, and their health is not tracked by the app. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
                }
                else{
                    currCombatantHp = npc.getHealth();
                    //TODO display some popup with the NPC's current health, and a box to determine how much health the DM wants to Add/Remove
                    int change = 0, overkill;
                    currCombatantHp -= change;
                    if(currCombatantHp < 0){
                        overkill = currCombatantHp;
                        currCombatantHp = 0;
                        if(overkill > npc.getMaxHealth()){ //If the current combatant would be outright killed by the damage
                            //TODO the combatant is killed by RAW so the DM should be asked if they want to change their state to DEAD
                        }
                        else{ //If the combatant is not outright killed the DM should be asked if they want to change their state to ALIVE, DEAD, UNCONSCIOUS or UNSTABLE
                            //TODO the combatant is at 0 health, ask the DM what their state should be changed to. Note it could still be ALIVE if a creature has HP regen
                        }
                    }
                    npc.setHealth(currCombatantHp);
                }
            }
        });

        //Button to change the combatant states (ALIVE, DEAD, STABLE, UNSTABLE)
//        Button changeStatusButton = findViewById(R.id.btnStatus);
//        changeStatusButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showStatusChangeMenu(v);
//                //TODO should probably also grey out/disabled the background while the menu is up to prevent the user from doing other things while the menu is open?
//            }
//        });

        //Button to roll death saving throws for the currently selected combatant
        Button rollDeathSaveButton = findViewById(R.id.btnDeathSave);
        rollDeathSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (isPlayer) { //Check if the current combatant is a player or not
                    Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, who should roll their own death saves. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(checkIfUnstable(npc)) { //Check if the npc is unstable and therefore if they need to roll a death save
                        NPC.deathSaveResult saveResult = npc.rolLDeathSave(0, 0); //TODO get the actual adv and bonus values from the DM
                        switch (saveResult){
                            case SUCCESS:

                                Toast.makeText(getApplicationContext(), "Rolled a success on a death save for: " + npc.getName() + ".", Toast.LENGTH_SHORT).show();
                                break;
                            case FAILURE:
                                npc.setNextDeathSave(saveResult);
                                Toast.makeText(getApplicationContext(), "Rolled a failure on a death save for: " + npc.getName() + ".", Toast.LENGTH_SHORT).show();
                                break;
                            case CRITICALSUCCESS:
                                npc.resetDeathSaves();
                                npc.setHealth(1);
                                npc.setCombatState(Combatant.combatantStates.ALIVE);
                                Toast.makeText(getApplicationContext(), "Rolled a critical success (natural 20) on a death save for: " + npc.getName() + ". They are alive and have 1 hp.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        switch (npc.checkDeathSaves()){
                            case UNSTABLE:
                                Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is still UNSTABLE.");
                                break;
                            case DEAD:
                                npc.resetDeathSaves();
                                npc.setCombatState(Combatant.combatantStates.DEAD);
                                Toast.makeText(getApplicationContext(), "The current combatant has failed 3 death saves and is now dead.", Toast.LENGTH_SHORT).show();
                                Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is now DEAD.");
                                break;
                            case STABLE:
                                npc.resetDeathSaves();
                                npc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                                Toast.makeText(getApplicationContext(), "The current combatant has succeeded 3 death saves and is now stable.", Toast.LENGTH_SHORT).show();
                                Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is now UNCONSCIOUS (Stable).");
                                break;
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "The current combatant is not unstable (unconscious) and cannot make death saving throws!.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Button to end the combat)
        Button endCombatButton = findViewById(R.id.btnEndCombat);
        endCombatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                combatComplete = true;
                //TODO confirm with the DM that they would like to end the combat. If saving is implemented ask if they want to save. End combat or cancel
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setCancelable(true);
                builder.setTitle("Combat Finish");
                builder.setMessage("Are you sure you would like to finish combat? You can't return to this combat later.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        gotoMainActivity();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

    @Override
    public boolean onMenuItemClick(MenuItem item){
        if(isPlayer){
            switch(item.getItemId()) {
                case R.id.csm_Alive:
                    pc.setCombatState(Combatant.combatantStates.ALIVE);
                    return true;

                case R.id.csm_Dead:
                    pc.setCombatState(Combatant.combatantStates.DEAD);
                    return true;

                case R.id.csm_Stable:
                    pc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                    return true;

                case R.id.csm_Unstable:
                    pc.setCombatState(Combatant.combatantStates.UNSTABLE);
                    return true;

                default:
                    return false;
            }
        }
        else{
            switch(item.getItemId()) {
                case R.id.csm_Alive:
                    npc.setCombatState(Combatant.combatantStates.ALIVE);
                    return true;

                case R.id.csm_Dead:
                    npc.setCombatState(Combatant.combatantStates.DEAD);
                    return true;

                case R.id.csm_Stable:
                   npc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                    return true;

                case R.id.csm_Unstable:
                    npc.setCombatState(Combatant.combatantStates.UNSTABLE);
                    return true;

                default:
                    return false;
            }
        }

    }

    private boolean checkIfUnstable(NPC npc){
        return (npc.getStatus() == Combatant.combatantStates.UNSTABLE);
    }

    private void showStatusChangeMenu(View v) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.change_state_menu);
        popup.show();
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}

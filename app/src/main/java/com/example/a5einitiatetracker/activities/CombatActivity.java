package com.example.a5einitiatetracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class CombatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListIterator<Combatant> iterator;
    List<Combatant> combatantList;
    Combatant currCombatant;
    NPC npc;
    Player pc;
    Boolean combatComplete, isPlayer;
    int count;
    TextView txtViewCombatantHealth, txtViewChangeHealth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iterator = combatantList.listIterator();
        combatComplete = false;

        //Initialize the TextViews
        txtViewCombatantHealth = findViewById(R.id.txtViewCombatantCurrentHealth);
        txtViewChangeHealth = findViewById(R.id.txtViewCombatantHealth);

        //Button to go to the previous combatant in initiative
        Button nextButton = findViewById(R.id.btnNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextCombatantOnClick();
            }
        });

        //Button to go to the next combatant in initiative
        Button previousButton = findViewById(R.id.btnPrev);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                previousCombatantOnClick();
            }
        });

        //Button to heal the combatant based on the number entered in the HP field
        Button healHpButton = findViewById(R.id.btnHpHeal);
        healHpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                healHpOnClick();
            }
        });

        //Button to damage the combatant based on the number entered in the HP field
        Button damageHpButton = findViewById(R.id.btnHpDamage);
        damageHpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                damageHpOnClick();
            }
        });

        //Button to roll death saving throws for the currently selected combatant
        Button rollDeathSaveButton = findViewById(R.id.btnDeathSave);
        rollDeathSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rollDeathSaveOnClick();
            }
        });

        //Button to end the combat
        Button endCombatButton = findViewById(R.id.btnEndCombat);
        endCombatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            endCombatOnClick();
            }
        });

        //combatantStatus Spinner
        Spinner statusSpinner = findViewById(R.id.combatantStatusSpinner);
        ArrayAdapter<CharSequence> statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);
        statusSpinner.setOnItemSelectedListener(this);
        statusSpinner.setPrompt("Status");
        if(isPlayer)
            statusSpinner.setSelection(getStatusSpinnerPosition(pc.getCombatState()));
        else
            statusSpinner.setSelection(getStatusSpinnerPosition(npc.getCombatState()));
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


    private boolean checkIfUnstable(NPC npc){
        return (npc.getStatus() == Combatant.combatantStates.UNSTABLE);
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    //region BUTTON METHODS
    private void endCombatOnClick(){
        combatComplete = true;
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

    private void nextCombatantOnClick(){
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
            Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant, change one of their states, or end the combat.", Toast.LENGTH_SHORT).show();
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

    private void previousCombatantOnClick(){
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
            Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant, change one of their states, or end the combat.", Toast.LENGTH_SHORT).show();
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

    private void rollDeathSaveOnClick() {
        if (isPlayer) { //Check if the current combatant is a player or not
            Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, who should roll their own death saves. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
        } else {
            if (checkIfUnstable(npc)) { //Check if the npc is unstable and therefore if they need to roll a death save
                NPC.deathSaveResult saveResult = npc.rolLDeathSave(0, 0); //TODO get the actual adv and bonus values from the DM
                switch (saveResult) {
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
                switch (npc.checkDeathSaves()) {
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
            } else {
                Toast.makeText(getApplicationContext(), "The current combatant is not unstable (unconscious) and cannot make death saving throws!.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void healHpOnClick(){
        int currCombatantHp;
        if(isPlayer){
            Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, and their health is not tracked by the app. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
        }
        else{
            currCombatantHp = npc.getHealth();
            int change = Integer.parseInt(txtViewChangeHealth.getText().toString());
            int maxHp = npc.getMaxHealth();

            currCombatantHp += change;
            if(currCombatantHp > maxHp)
                currCombatantHp = maxHp;

            txtViewCombatantHealth.setText(currCombatantHp);
            txtViewChangeHealth.setText("0");
            npc.setHealth(currCombatantHp);
        }
    }

    private void damageHpOnClick(){
        int currCombatantHp;
        if(isPlayer){
            Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, and their health is not tracked by the app. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
        }
        else{
            currCombatantHp = npc.getHealth();
            int change = Integer.parseInt(txtViewChangeHealth.getText().toString());

            currCombatantHp -= change;
            if(currCombatantHp < 0){
                int overkill = Math.abs(currCombatantHp);
                currCombatantHp = 0;
                if(overkill > npc.getMaxHealth()){ //If the current combatant would be outright killed by the damage
                    //TODO the combatant is killed by RAW so the DM should be asked if they want to change their state to DEAD
                    Log.d("healHpClick", "The combatant: " + npc.getName() + " is killed by RAW.");
                }
                else{ //If the combatant is not outright killed the DM should be asked if they want to change their state to ALIVE, DEAD, UNCONSCIOUS or UNSTABLE
                    //TODO the combatant is at 0 health, ask the DM what their state should be changed to. Note it could still be ALIVE if a creature has HP regen
                    Log.d("healHpClick", "The combatant: " + npc.getName() + " is at 0 HP");
                }
            }
            txtViewCombatantHealth.setText(currCombatantHp);
            txtViewChangeHealth.setText("0");
            npc.setHealth(currCombatantHp);
        }
    }
    //endregion

    //region STATUS SPINNER METHODS
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);

        if(isPlayer) {
            switch (item) {
                case "Alive":
                    pc.setCombatState(Combatant.combatantStates.ALIVE);
                    break;
                case "Dead":
                    pc.setCombatState(Combatant.combatantStates.DEAD);
                    break;
                case "Stable":
                    pc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                    break;
                case "Unstable":
                    pc.setCombatState(Combatant.combatantStates.UNSTABLE);
                    break;
            }
        }
        else{
            switch (item) {
                case "Alive":
                    npc.setCombatState(Combatant.combatantStates.ALIVE);
                    break;
                case "Dead":
                    npc.setCombatState(Combatant.combatantStates.DEAD);
                    break;
                case "Stable":
                    npc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                    break;
                case "Unstable":
                    npc.setCombatState(Combatant.combatantStates.UNSTABLE);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Simple method to return the position of the string in the Strings.xml resource for any of the 4 conditions
    private int getStatusSpinnerPosition(Combatant.combatantStates status){
        switch (status){
            case ALIVE:
                return 0;
            case DEAD:
                return 1;
            case UNCONSCIOUS:
                return 2;
            case UNSTABLE:
                return 3;
            default:
                return 0;
        }
    }
    //endregion
}

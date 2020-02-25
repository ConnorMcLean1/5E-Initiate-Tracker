package com.example.a5einitiatetracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class CombatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<Combatant> combatantList;
    Combatant currCombatant;
    NPC npc;
    Player pc;
    Boolean combatComplete, isPlayer;
    int count, currentIndex;
    TextView txtViewCombatantHealth, txtViewCombatantName;
    EditText editTextChangeHealth;
    Button previousButton, nextButton, healHpButton, damageHpButton, rollDeathSaveButton, endCombatButton;
    Spinner statusSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        //region TEST VARIABLES
        combatantList = new ArrayList<>();
        combatantList.add(new Player(22, 5, Combatant.combatantStates.ALIVE, "Player1"));
        combatantList.add(new NPC(2, Combatant.combatantStates.ALIVE, 100, "Goblin 1", 0));
        combatantList.add(new NPC(2, Combatant.combatantStates.ALIVE, 100, "Goblin 2", 0));
        combatantList.add(new NPC(2, Combatant.combatantStates.ALIVE, 100, "Goblin 3", 0));
        //endregion

        currentIndex = 0;
        combatComplete = false;

        //Initialize the TextViews
        txtViewCombatantHealth = findViewById(R.id.txtViewCombatantCurrentHealth);
        txtViewCombatantName = findViewById(R.id.txtViewCombatantName);

        //Initialize the EditTexts
        editTextChangeHealth = findViewById(R.id.editTxtHealth);

        //Button to go to the previous combatant in initiative
        nextButton = findViewById(R.id.btnNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextCombatantOnClick();
            }
        });
        //Button to go to the next combatant in initiative
        previousButton = findViewById(R.id.btnPrev);
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                previousCombatantOnClick();
            }
        });

        //Button to heal the combatant based on the number entered in the HP field
        healHpButton = findViewById(R.id.btnHpHeal);
        healHpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                healHpOnClick();
            }
        });

        //Button to damage the combatant based on the number entered in the HP field
        damageHpButton = findViewById(R.id.btnHpDamage);
        damageHpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                damageHpOnClick();
            }
        });

        //Button to roll death saving throws for the currently selected combatant
        rollDeathSaveButton = findViewById(R.id.btnDeathSave);
        rollDeathSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rollDeathSaveOnClick();
            }
        });

        //Button to end the combat
        endCombatButton = findViewById(R.id.btnEndCombat);
        endCombatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            endCombatOnClick();
            }
        });

        //combatantStatus Spinner
        statusSpinner = findViewById(R.id.combatantStatusSpinner);
        ArrayAdapter<CharSequence> statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);
        statusSpinner.setOnItemSelectedListener(this);
        statusSpinner.setPrompt("Status");

        //region INITIAL SCREEN SETUP
        //TODO should change the below to find the first non-dead combatant most likely
        //Setup combat screen using the first combatant in the list
        currCombatant = combatantList.get(0);
        if (currCombatant instanceof Player) {
            isPlayer = true;
            pc = (Player) currCombatant;
            updateUIValues();
        }
        else {
            isPlayer = false;
            npc = (NPC) currCombatant;
            updateUIValues();
        }

        editTextChangeHealth.setText("0");
        //endregion
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setCancelable(true);
        builder.setTitle("Combat Finish");
        builder.setMessage("Are you sure you would like to finish combat? You can't return to this combat later.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                gotoMainActivity();
                combatantList.clear();
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
            if (currentIndex+1 < combatantList.size()) { //Check if there is another combatant in the list. If yes, grab it out
                currentIndex++;
                currCombatant = combatantList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST","Next");
            }
            else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                currentIndex = 0;
                count++;
                currCombatant = combatantList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST", "Next Button. No next combatant. Reset to start of iterator");
            }
        } while(currCombatant.getCombatState() == Combatant.combatantStates.DEAD && count < 2);

        if(count > 1){ //If the count goes over 1 the do/while likely would have gone on infinitely. The combat should end at this point, as there is nothing left to do
            Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant, change one of their states, or end the combat.", Toast.LENGTH_SHORT).show();
            Log.d("MAIN_LOOP_TEST", "Next Button. No non-dead combatants. Loop would be infinite");
        }

        if(currCombatant instanceof Player){ //Check if the current combatant is a player or not, and cast it appropriately
            pc = (Player) currCombatant;
            isPlayer = true;
            updateUIValues();
            Log.d("MAIN_LOOP_TEST", "Next Button. The current combatant: " + pc.getName() + " is a PC.");
        }
        else{
            npc = (NPC) currCombatant;
            isPlayer = false;
            updateUIValues();
            Log.d("MAIN_LOOP_TEST", "Next Button. The current combatant: " + npc.getName() + " is a NPC.");
        }
    }

    private void previousCombatantOnClick(){
        count = 0; //Counter variable to prevent infinite looping if the combat only contains dead characters

        do { //Get the next combatant, skipping over dead ones
            if (currentIndex-1 >= 0) { //Check if there is another combatant in the list. If yes, grab it out
                currentIndex--;
                currCombatant = combatantList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST","Previous");
            } else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                currentIndex = combatantList.size()-1;
                count++;
                currCombatant = combatantList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST", "Previous Button. No previous combatant. Reset to end of iterator");
            }
        } while (currCombatant.getCombatState() == Combatant.combatantStates.DEAD && count < 2);

        if (count > 1) { //If the count goes over 1 the do/while likely would have gone on infinitely. The combat should end at this point, as there is nothing left to do
            Toast.makeText(getApplicationContext(), "The combat contains no non-dead combatants. Add another combatant, change one of their states, or end the combat.", Toast.LENGTH_SHORT).show();
            Log.d("MAIN_LOOP_TEST", "Previous Button. No non-dead combatants. Loop would be infinite");
        }

        if (currCombatant instanceof Player) { //Check if the current combatant is a player or not, and cast it appropriately
            pc = (Player) currCombatant;
            isPlayer = true;
            updateUIValues();
            Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + pc.getName() + " is a PC.");
        }
        else {
            npc = (NPC) currCombatant;
            isPlayer = false;
            updateUIValues();
            Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + npc.getName() + " is a NPC.");
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
                        npc.setNextDeathSave(saveResult);
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
                        Log.d("MAIN_LOOP_TEST", "Death Save. The current combatant rolled a death save and is still UNSTABLE.");
                        break;
                    case DEAD:
                        npc.resetDeathSaves();
                        npc.setCombatState(Combatant.combatantStates.DEAD);
                        Toast.makeText(getApplicationContext(), "The current combatant has failed 3 death saves and is now dead.", Toast.LENGTH_SHORT).show();
                        Log.d("MAIN_LOOP_TEST", "Death Save. The current combatant rolled a death save and is now DEAD.");
                        break;
                    case STABLE:
                        npc.resetDeathSaves();
                        npc.setCombatState(Combatant.combatantStates.UNCONSCIOUS);
                        Toast.makeText(getApplicationContext(), "The current combatant has succeeded 3 death saves and is now stable.", Toast.LENGTH_SHORT).show();
                        Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is now UNCONSCIOUS (Stable).");
                        break;
                }
                updateUIValues();
                Log.d("MAIN_LOOP_TEST","Checking death saves. Current saves are: " + Arrays.toString(npc.getDeathSaves()));
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
            int change = Integer.parseInt(editTextChangeHealth.getText().toString());
            Log.d("MAIN_LOOP_TEST","ChangeHp: the combatant: " + npc.getName() +" now has: " + npc.getHealth() + " health.");
            int maxHp = npc.getMaxHealth();

            currCombatantHp += change;
            if(currCombatantHp > maxHp)
                currCombatantHp = maxHp;

            editTextChangeHealth.setText("0");
            npc.setHealth(currCombatantHp);
            updateHealth();
        }
    }

    private void damageHpOnClick(){
        int currCombatantHp;
        if(isPlayer){
            Toast.makeText(getApplicationContext(), "The currently selected combatant is a player character, and their health is not tracked by the app. Please select a non-player character and try again!", Toast.LENGTH_SHORT).show();
        }
        else{
            currCombatantHp = npc.getHealth();
            int change = Integer.parseInt(editTextChangeHealth.getText().toString());

            currCombatantHp -= change;
            if(currCombatantHp < 0){
                int overkill = Math.abs(currCombatantHp);
                currCombatantHp = 0;
                if(overkill >= npc.getMaxHealth()){ //If the current combatant would be outright killed by the damage
                    //TODO the combatant is killed by RAW so the DM should be asked if they want to change their state to DEAD
                    Log.d("healHpClick", "The combatant: " + npc.getName() + " is killed by RAW.");
                }
                else{ //If the combatant is not outright killed the DM should be asked if they want to change their state to ALIVE, DEAD, UNCONSCIOUS or UNSTABLE
                    //TODO the combatant is at 0 health, ask the DM what their state should be changed to. Note it could still be ALIVE if a creature has HP regen
                    Log.d("healHpClick", "The combatant: " + npc.getName() + " is at 0 HP");
                }
            }

            editTextChangeHealth.setText("0");
            npc.setHealth(currCombatantHp);
            updateHealth();
        }
    }
    //endregion

    //region STATUS SPINNER METHODS
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        //TODO add handling to prevent the combatant status from changing if they are not at the correct HP for the status OR change the HP as well
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
                Log.d("SPINNER_POS","ALIVE, 0");
                return 0;
            case DEAD:
                Log.d("SPINNER_POS","DEAD, 1");
                return 1;
            case UNCONSCIOUS:
                Log.d("SPINNER_POS","UNCONSCIOUS, 2");
                return 2;
            case UNSTABLE:
                Log.d("SPINNER_POS","UNSTABLE, 3");
                return 3;
            default:
                Log.d("SPINNER_POS","Default, 0");
                return 0;
        }
    }
    //endregion

    //region UI UPDATERS
    private void updateUIValues(){
        updateHealth();
        updateName();
        updateStatus();
    }

    private void updateHealth(){
        if(isPlayer)
            txtViewCombatantHealth.setText(R.string.not_tracked);
        else
            txtViewCombatantHealth.setText(Integer.toString(npc.getHealth()));
    }

    private void updateStatus(){
        if(isPlayer)
            statusSpinner.setSelection(getStatusSpinnerPosition(pc.getCombatState()));
        else
            statusSpinner.setSelection(getStatusSpinnerPosition(npc.getCombatState()));
    }

    private void updateName(){
        if(isPlayer)
            txtViewCombatantName.setText(pc.getName());
        else
            txtViewCombatantName.setText(npc.getName());
    }
    //endregion
}

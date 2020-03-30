package com.example.a5einitiatetracker.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.api.json.JSONUtility;
import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.NPC;
import com.example.a5einitiatetracker.combatant.Player;
import com.example.a5einitiatetracker.dialogs.CombatantsDialog;
import com.example.a5einitiatetracker.views.VerticalRatingBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CombatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CombatantsDialog.CombatantsDialogListener {
    List<Combatant> combatantsList;
    public static List<NPC> npcs;
    Combatant currCombatant, prevCombatant, nextCombatant;
    NPC npc, previewNpc;
    Player pc, previewPc;
    Boolean combatComplete, isPlayer;
    int count, currentIndex;
    TextView txtViewCombatantHealth, txtViewCombatantName, txtViewNextCombatantPreview,
            txtViewPrevCombatantPreview, txtViewDeathSaves, txtViewChangeHp,
            txtViewCurrentHpLabel, txtViewInitiative, txtViewDeathSaveSuccessLabel,
            txtViewDeathSaveFailureLabel;
    EditText editTextChangeHealth, editTextDamageAmount;
    Button rollDeathSaveButton, dealDamageButton, saveCombatButton;
    ImageButton  endCombatButton, damageHpButton, healHpButton, previousButton, nextButton;
    Spinner statusSpinner;
    VerticalRatingBar deathSaveSuccessBar, deathSaveFailureBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        //Gets an intent to determine if this combat is one loaded previously or just from the
        //'next' button being clicked in the CombatantsActivity
        Intent thisIntent = getIntent();
        if(thisIntent.getBooleanExtra("isSaved", false)) {
            combatantsList = JSONUtility.loadCombatFromJSON(this.getApplicationContext(), JSONUtility.JSON_COMBAT_SAVED_FILE_NAME);
            //currentIndex = JSONUtility.loadCombatPositionFromJSON(this.getApplicationContext(), JSONUtility.JSON_COMBAT_SAVED_FILE_NAME);
        }
        else
            combatantsList = JSONUtility.loadCombatFromJSON(this.getApplicationContext(), JSONUtility.JSON_COMBAT_CURRENT_FILE_NAME);
        //combatantsList = CombatantsActivity.combatantsList;

        currentIndex = 0;
        combatComplete = false;

        //Initialize the TextViews
        txtViewCombatantHealth = findViewById(R.id.txtViewCombatantCurrentHealth);
        txtViewCombatantName = findViewById(R.id.txtViewCombatantName);
        txtViewNextCombatantPreview = findViewById(R.id.txtViewNextCombatantPreview);
        txtViewPrevCombatantPreview = findViewById(R.id.txtViewPrevCombatantPreview);
        txtViewChangeHp = findViewById(R.id.txtViewCombatantHealth);
        txtViewDeathSaves = findViewById(R.id.txtViewCombatantDeathSaves);
        txtViewCurrentHpLabel = findViewById(R.id.txtViewCombatantCurrentHealthLabel);
        txtViewInitiative = findViewById(R.id.txtViewInitiative);
        txtViewDeathSaveSuccessLabel = findViewById(R.id.txtViewDeathSaveSuccessBarLabel);
        txtViewDeathSaveFailureLabel = findViewById(R.id.txtViewDeathSaveFailureBarLabel);

        //Initialize the EditTexts
        editTextChangeHealth = findViewById(R.id.editTxtHealth);
        editTextDamageAmount = findViewById(R.id.editTxtDamageAmount);

        //Initialize the RatingBars
        deathSaveFailureBar = findViewById(R.id.deathSaveFailureBar);
        deathSaveSuccessBar = findViewById(R.id.deathSaveSuccessBar);

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

        saveCombatButton = findViewById(R.id.saveCombatButton);
        saveCombatButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                saveCombat();
            }
        });

        // Button to deal damage from player screen
        dealDamageButton = findViewById(R.id.btnDamage);
        dealDamageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNPCDialog();
                hideKeyboard(CombatActivity.this);
            }
        });

        // Edittext for entering damage amount to deal
        editTextDamageAmount = findViewById(R.id.editTxtDamageAmount);
        editTextDamageAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextDamageAmount.getText().toString().length() > 0) {
                    dealDamageButton.setEnabled(true);
                } else {
                    dealDamageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        //Setup combat screen using the first combatant in the list
        currCombatant = combatantsList.get(0);
        if (currCombatant instanceof Player) {
            isPlayer = true;
            pc = (Player) currCombatant;
        }
        else {
            isPlayer = false;
            npc = (NPC) currCombatant;
        }
        updateUIValues();
        updateControls();

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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
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
    private void openNPCDialog() {
        if (editTextDamageAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Enter damage to deal", Toast.LENGTH_SHORT).show();
        } else {
            int dmg = Integer.parseInt(editTextDamageAmount.getText().toString());
            npcs = combatantsList
                    .stream()
                    .filter(p -> p instanceof NPC)
                    .filter(p -> p.getCombatState() != NPC.combatantStates.DEAD)
                    .map(p -> (NPC)p)
                    .collect(Collectors.toList());
            Log.v("NPCs", npcs.toString());
            CombatantsDialog dialog = new CombatantsDialog();
            dialog.show(getSupportFragmentManager(), "combatants dialog");
            dialog.setDamage(dmg);
        }
    }

    @Override
    public void returnCombatantsList(ArrayList<NPC> checkedNPCs, int damage) {
        for (int i = 0; i < npcs.size(); i++) {
            for (int j = 0; j < checkedNPCs.size(); j++) {
                if (npcs.get(i).equals(checkedNPCs.get(j))) {
                    npcs.get(i).damageNpc(damage, this);
                }
            }
        }
        editTextDamageAmount.setText("");
        Toast.makeText(this, "Damage dealt!", Toast.LENGTH_SHORT).show();
    }

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
                combatantsList.clear();
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
            if (currentIndex+1 < combatantsList.size()) { //Check if there is another combatant in the list. If yes, grab it out
                currentIndex++;
                currCombatant = combatantsList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST","Next");
            }
            else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                currentIndex = 0;
                count++;
                currCombatant = combatantsList.get(currentIndex);
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
            updateControls();
            Log.d("MAIN_LOOP_TEST", "Next Button. The current combatant: " + pc.getName() + " is a PC.");
        }
        else{
            npc = (NPC) currCombatant;
            isPlayer = false;
            updateUIValues();
            updateControls();
            Log.d("MAIN_LOOP_TEST", "Next Button. The current combatant: " + npc.getName() + " is a NPC.");
        }
    }

    private void previousCombatantOnClick(){
        count = 0; //Counter variable to prevent infinite looping if the combat only contains dead characters

        do { //Get the next combatant, skipping over dead ones
            if (currentIndex-1 >= 0) { //Check if there is another combatant in the list. If yes, grab it out
                currentIndex--;
                currCombatant = combatantsList.get(currentIndex);
                Log.d("MAIN_LOOP_TEST","Previous");
            } else { //If not, the iterator is at the end of the list. Loop it back to the beginning
                currentIndex = combatantsList.size()-1;
                count++;
                currCombatant = combatantsList.get(currentIndex);
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
            updateControls();
            Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + pc.getName() + " is a PC.");
        }
        else {
            npc = (NPC) currCombatant;
            isPlayer = false;
            updateUIValues();
            updateControls();
            Log.d("MAIN_LOOP_TEST", "Previous Button. The current combatant: " + npc.getName() + " is a NPC.");
        }
    }

    private void rollDeathSaveOnClick() {
        if (checkIfUnstable(npc)) { //Check if the npc is unstable and therefore if they need to roll a death save
            npc.rollDeathSave(0, 0); //TODO get the advantage and bonus values from the DM if needed
            Log.d("MAIN_LOOP_TEST","Checking death saves. Current saves are: " + Arrays.toString(npc.getDeathSaves()));
            checkDeathSaves();
            updateUIValues();
        } else {
            Toast.makeText(getApplicationContext(), "The current combatant is not unstable (unconscious) and cannot make death saving throws!.", Toast.LENGTH_SHORT).show();
        }
    }

    private void healHpOnClick(){
        int currCombatantHp;
        if(npc.getCombatState() == Combatant.combatantStates.DEAD){
            Toast.makeText(getApplicationContext(), "The combatant is dead and cannot be healed.", Toast.LENGTH_SHORT).show();
            editTextChangeHealth.setText("0");
            updateControls();
            updateUIValues();
        }
            int change = Integer.parseInt(editTextChangeHealth.getText().toString());

            if(change > 0) {
                currCombatantHp = npc.getHealth();
                int maxHp = npc.getMaxHealth();

                currCombatantHp += change;
                if (currCombatantHp > maxHp)
                    currCombatantHp = maxHp;

                editTextChangeHealth.setText("0");
                npc.setHealth(currCombatantHp);
                Log.d("MAIN_LOOP_TEST", "ChangeHp: the combatant: " + npc.getName() + " now has: " + npc.getHealth() + " health.");

                if ((npc.getCombatState() == Combatant.combatantStates.UNSTABLE || npc.getCombatState() == Combatant.combatantStates.UNCONSCIOUS) && npc.getHealth() > 0) {
                    npc.setCombatState(Combatant.combatantStates.ALIVE);
                    statusSpinner.setSelection(getStatusSpinnerPosition(Combatant.combatantStates.ALIVE));
                    Toast.makeText(getApplicationContext(), "The combatant has been healed and is no longer unstable.", Toast.LENGTH_SHORT).show();
            }

            updateControls();
            updateControls();
            updateUIValues();
        }
            hideKeyboard(CombatActivity.this);
    }

    private void damageHpOnClick(){
        //Setup the damage variable and grab it from the edit text. If the an invalid entry exists,
        //simply default to 0 so there is no change
        int damage;
        try{
            damage = Integer.parseInt(editTextChangeHealth.getText().toString());
        }
        catch(NumberFormatException e){
            damage = 0;
        }
        boolean checkSaves = npc.damageNpc(damage, getApplicationContext());

        //If the NPC has suffered a failed death save due to the damage, update the UI
        if(checkSaves)
            checkDeathSaves();

        //Reset text box to 0 and update controls
        editTextChangeHealth.setText("0");
        updateUIValues();
        updateControls();
        hideKeyboard(CombatActivity.this);
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
        updateUIValues();
        updateControls();
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
        updatePreviews();
        updateInitiative();
        updateDamageControls();
        updateDeathSaveBars();
    }

    private void updateControls(){
        updateDeathSaveButtons();
        updateHpControls();
        updateDamageControls();
    }

    private void updateInitiative(){
        if(isPlayer){
            txtViewInitiative.setText(Integer.toString(pc.getInitiative()));
        }
        else{
            txtViewInitiative.setText(Integer.toString(npc.getInitiative()));
        }
    }

    private void updateHealth(){
        if(isPlayer) {
            txtViewCombatantHealth.setText(R.string.not_tracked);
            txtViewCombatantHealth.setVisibility(View.GONE);
            txtViewCurrentHpLabel.setVisibility(View.GONE);
        }
        else {
            txtViewCombatantHealth.setText(
                    String.format(Locale.CANADA,
                            "%d / %d",
                            npc.getHealth(), npc.getMaxHealth())
            );
            String temp = Integer.toString(npc.getHealth()) + " / " + Integer.toString(npc.getMaxHealth());
            txtViewCombatantHealth.setText(temp);
            txtViewCombatantHealth.setVisibility(View.VISIBLE);
            txtViewCurrentHpLabel.setVisibility(View.VISIBLE);
        }
    }

    private void updateDamageControls() {
        if (isPlayer) {
            dealDamageButton.setEnabled(false);
            editTextDamageAmount.setVisibility(View.VISIBLE);
            dealDamageButton.setVisibility(View.VISIBLE);
        } else {
            editTextDamageAmount.setVisibility(View.GONE);
            dealDamageButton.setVisibility(View.GONE);
        }
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

    private void updatePreviews(){

        previewNpc = npc;
        previewPc = pc;

        if (currentIndex+1 < combatantsList.size()) nextCombatant = combatantsList.get(currentIndex+1);
        else nextCombatant = combatantsList.get(0);

        if (currentIndex-1 >= 0) prevCombatant = combatantsList.get(currentIndex-1);
        else prevCombatant = combatantsList.get(combatantsList.size()-1);

        if (nextCombatant instanceof Player) {
            previewPc = (Player) nextCombatant;
            txtViewNextCombatantPreview.setText(previewPc.getName());
        }
        else {
            previewNpc = (NPC) nextCombatant;
            txtViewNextCombatantPreview.setText(previewNpc.getName());
        }

        if (prevCombatant instanceof Player) {
            previewPc = (Player) prevCombatant;
            txtViewPrevCombatantPreview.setText(previewPc.getName());
        }
        else {
            previewNpc = (NPC) prevCombatant;
            txtViewPrevCombatantPreview.setText(previewNpc.getName());
        }
    }

    private void updateDeathSaveButtons(){
        if(isPlayer) {
            rollDeathSaveButton.setEnabled(false);
            rollDeathSaveButton.setVisibility(View.GONE);
            txtViewDeathSaves.setVisibility(View.GONE);
            deathSaveSuccessBar.setVisibility(View.GONE);
            deathSaveFailureBar.setVisibility(View.GONE);
            txtViewDeathSaveFailureLabel.setVisibility(View.GONE);
            txtViewDeathSaveSuccessLabel.setVisibility(View.GONE);
        }
        else if(npc.getCombatState() != Combatant.combatantStates.UNSTABLE){
            rollDeathSaveButton.setEnabled(false);
            rollDeathSaveButton.setVisibility(View.VISIBLE);
            txtViewDeathSaves.setVisibility(View.VISIBLE);
            deathSaveSuccessBar.setVisibility(View.VISIBLE);
            deathSaveFailureBar.setVisibility(View.VISIBLE);
            txtViewDeathSaveFailureLabel.setVisibility(View.VISIBLE);
            txtViewDeathSaveSuccessLabel.setVisibility(View.VISIBLE);
        }
        else {
            rollDeathSaveButton.setEnabled(true);
            rollDeathSaveButton.setVisibility(View.VISIBLE);
            txtViewDeathSaves.setVisibility(View.VISIBLE);
            deathSaveSuccessBar.setVisibility(View.VISIBLE);
            deathSaveFailureBar.setVisibility(View.VISIBLE);
            txtViewDeathSaveFailureLabel.setVisibility(View.VISIBLE);
            txtViewDeathSaveSuccessLabel.setVisibility(View.VISIBLE);
        }

    }

    private void updateDeathSaveBars(){
        if(!isPlayer) {
            deathSaveSuccessBar.setRating(npc.getDeathSaveSuccesses());
            deathSaveFailureBar.setRating(npc.getDeathSaveFailures());
        }
    }

    private void updateHpControls(){
        if(isPlayer) {
            damageHpButton.setEnabled(false);
            damageHpButton.setVisibility(View.GONE);
            healHpButton.setEnabled(false);
            healHpButton.setVisibility(View.GONE);
            editTextChangeHealth.setEnabled(false);
            editTextChangeHealth.setVisibility(View.GONE);
            txtViewChangeHp.setVisibility(View.GONE);
        }
        else {
            damageHpButton.setEnabled(true);
            damageHpButton.setVisibility(View.VISIBLE);
            healHpButton.setEnabled(true);
            healHpButton.setVisibility(View.VISIBLE);
            editTextChangeHealth.setEnabled(true);
            editTextChangeHealth.setVisibility(View.VISIBLE);
            txtViewChangeHp.setVisibility(View.VISIBLE);
        }
    }

    private void saveCombat(){
        JSONUtility.saveCombatToJSON(combatantsList, currentIndex, JSONUtility.JSON_COMBAT_SAVED_FILE_NAME, this.getApplicationContext());
        Toast.makeText(this.getApplicationContext(), "Combat saved successfully!", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //Method to handle the return from the NPC.checkDeathSaves() method
    //updates the NPC's values and also sets the spinner to the correct position

    //region UTILITY METHODS
    private void checkDeathSaves(){
        switch (npc.checkDeathSaves()) {
            case UNSTABLE:
                Log.d("MAIN_LOOP_TEST", "Death Save. The current combatant rolled a death save and is still UNSTABLE.");
                break;
            case DEAD:
                npc.resetDeathSaves();
                npc.setStatus(Combatant.combatantStates.DEAD);
                statusSpinner.setSelection(getStatusSpinnerPosition(Combatant.combatantStates.DEAD));
                Toast.makeText(getApplicationContext(), "The current combatant has failed 3 death saves and is now dead.", Toast.LENGTH_SHORT).show();
                Log.d("MAIN_LOOP_TEST", "Death Save. The current combatant rolled a death save and is now DEAD.");
                break;
            case STABLE:
                npc.resetDeathSaves();
                npc.setStatus(Combatant.combatantStates.UNCONSCIOUS);
                statusSpinner.setSelection(getStatusSpinnerPosition(Combatant.combatantStates.UNCONSCIOUS));
                Toast.makeText(getApplicationContext(), "The current combatant has succeeded 3 death saves and is now stable.", Toast.LENGTH_SHORT).show();
                Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is now UNCONSCIOUS (Stable).");
                break;
            case ALIVE:
                npc.resetDeathSaves();
                npc.setStatus(Combatant.combatantStates.ALIVE);
                npc.setHealth(1);
                statusSpinner.setSelection(getStatusSpinnerPosition(Combatant.combatantStates.ALIVE));
                Toast.makeText(getApplicationContext(), "The current combatant has critically succeeded and is alive again.", Toast.LENGTH_SHORT).show();
                Log.d("MAIN_LOOP_TEST", "Death Save. The current combat rolled a death save and is now ALIVE.");
                break;

        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //endregion

}

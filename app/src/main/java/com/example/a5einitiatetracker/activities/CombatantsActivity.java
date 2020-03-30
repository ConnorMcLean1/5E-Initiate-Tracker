package com.example.a5einitiatetracker.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.api.APIUtility;
import com.example.a5einitiatetracker.api.json.JSONUtility;
import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.NPC;
import com.example.a5einitiatetracker.combatant.Player;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombatantsActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    HashMap<String, String> monsterNames;
    public static List<Combatant> combatantsList = new ArrayList<>();
    private boolean isValid;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combatants);

        parentLinearLayout = findViewById(R.id.lnrLayoutMonsters);
        //Reads in the list of monster Names and Indexes from the JSON file created on startup
        monsterNames = JSONUtility.readMonsternamesFromJSONFile(this.getApplicationContext(), JSONUtility.JSON_FILE_NAME);

        String[] monsters = new String[monsterNames.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : monsterNames.entrySet()) {
            monsters[i] = entry.getKey();
            Log.d("myTAG", monsters[i]);
            i++;
        }


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, monsters);
        // add monster/enemy
        FloatingActionButton addMonsterButton = findViewById(R.id.fabAddMonster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.monster_entry_layout, null);
                final AutoCompleteTextView autoCompleteTextView = rowView.findViewById(R.id.autoTxtViewMonsters);
                autoCompleteTextView.setAdapter(arrayAdapter);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        });
        // add player character
        FloatingActionButton addPlayerButton = findViewById(R.id.fabAddPlayer);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.player_entry_layout, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        });
        // add custom npc
        FloatingActionButton addCustomNPC = findViewById(R.id.fabAddCustomNPC);
        addCustomNPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.custom_npc_entry_layout, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        combatantsList.clear();
    }

    public void startCombat() {
        JSONUtility.saveCombatToJSON(combatantsList, 0, JSONUtility.JSON_COMBAT_CURRENT_FILE_NAME,  this.getApplicationContext());
        Intent intent = new Intent(getBaseContext(), CombatActivity.class);
        intent.putExtra("isSaved", false);
        startActivity(intent);
    }

    // loads all the monster info and adds them to the combatant list, then sorts the list by
    // initiative
    public void getMonsterData(View v) {
        //If the combatants list is already created then clear it before adding new elements to it
        if (!combatantsList.isEmpty())
            combatantsList.clear();
        //If the user tries to start an empty combat, prevent it and display a message
        if (parentLinearLayout.getChildCount() == 0) {
            Toast.makeText(this.getApplicationContext(), "Please add at least one combatant to the combat to start it!", Toast.LENGTH_SHORT).show();
            return;
        }
        isValid = true;
        checkMonsterQuantityValidity();
        checkMonstersValidity();
        final HashMap<String, Integer> monsters = createMonsterKeyValuePair();
        if (isValid) {
            getPlayers();
            getCustomNPCs();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String monsterIndex;
                    String monsterName;
                    for (HashMap.Entry<String, Integer> entry : monsters.entrySet()) {
                        monsterIndex = monsterNames.get(entry.getKey());
                        for (int i = 0; i < entry.getValue(); i++) {
                            NPC m = APIUtility.getMonsterByIndex(monsterIndex);
                            monsterName = String.format("%s %d", m.getName(), i + 1);
                            m.setName(monsterName);
                            combatantsList.add(m);
                        }
                    }

                    Collections.sort(combatantsList, Collections.<Combatant>reverseOrder());
                    Log.v("LIST", combatantsList.toString());
                    startCombat();
                }
            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(sb.toString())
                    .setTitle("Errors");
            AlertDialog dialog = builder.show();
            sb = new StringBuilder();
            //Toast.makeText(this.getApplicationContext(), "One of the monsters is not valid. Please ensure all fields are valid before continuing.", Toast.LENGTH_SHORT).show();
        }
    }

    // loads all the players into the combatant list
    private void getPlayers() {
        int combatantCount = parentLinearLayout.getChildCount();
        View v;
        EditText playerNameEditText, playerInitiativeEditText;
        String name;
        int initiative;
        for (int i = 0; i < combatantCount; i++) {
            v = parentLinearLayout.getChildAt(i);
            if (v.getTag().toString().equals("player_entry")) {
                playerNameEditText = v.findViewById(R.id.editTxtPlayerName);
                playerInitiativeEditText = v.findViewById(R.id.editTxtInitiative);
                name = playerNameEditText.getText().toString();
                initiative = Integer.parseInt(playerInitiativeEditText.getText().toString());
                Player p = new Player(initiative, name);
                combatantsList.add(p);
            }
        }
    }

    private void getCustomNPCs() {
        int combatantCount = parentLinearLayout.getChildCount();
        View v;
        EditText npcName, npcInitiavtive, npcHealth, npcAC;
        String name;
        int initiative, health, ac;
        for (int i = 0; i < combatantCount; i++) {
            v = parentLinearLayout.getChildAt(i);
            if (v.getTag().toString().equals("custom_npc_entry")) {
                npcName = v.findViewById(R.id.editTxtCustomNPCName);
                npcInitiavtive = v.findViewById(R.id.editTxtCustomNPCInitiative);
                npcHealth = v.findViewById(R.id.editTxtCustomNPCHealth);
                npcAC = v.findViewById(R.id.editTxtCustomNPCAC);
                name = npcName.getText().toString();
                initiative = Integer.parseInt(npcInitiavtive.getText().toString());
                health = Integer.parseInt(npcHealth.getText().toString());
                ac = Integer.parseInt(npcAC.getText().toString());
                NPC c = new NPC(name, initiative, health, ac);
                combatantsList.add(c);
            }
        }
    }

    // creates a hashmap of all the monsters with their name and the number to load
    private HashMap createMonsterKeyValuePair() {
        HashMap<String, Integer> m = new HashMap<String, Integer>();
        int combatantCount = parentLinearLayout.getChildCount();
        View view;
        AutoCompleteTextView name;
        EditText num;
        for (int i = 0; i < combatantCount; i++) {
            view = parentLinearLayout.getChildAt(i);
            if (view.getTag().toString().equals("monster_entry")) {
                num = view.findViewById(R.id.editTxtMonsterNumber);
                name = view.findViewById(R.id.autoTxtViewMonsters);
                if (m.containsKey(name.getText().toString())) {
                    isValid = false;
                    sb.append(String.format("The monster %s has already been added, please delete one.\n", name.getText().toString()));
                } else if(isValid) {
                    m.put(
                            name.getText().toString(),
                            Integer.parseInt(num.getText().toString())
                    );
                }
            }
        }
        return m;
    }

    private void checkMonstersValidity() {
        int combatantCount = parentLinearLayout.getChildCount();
        View view;
        AutoCompleteTextView name;
        EditText num;
        Log.d("TEST","Checking Monster Quantity Validity");
        for (int i = 0; i < combatantCount; i++) {
            view = parentLinearLayout.getChildAt(i);
            if (view.getTag().toString().equals("monster_entry")) {
                name = view.findViewById(R.id.autoTxtViewMonsters);
                String monster = name.getText().toString();
                if (monsterNames.containsKey(monster)) {
                    name.setBackgroundColor(Color.parseColor("#ffffff"));
                    Log.d("TEST","Monster Quantity Valid");
                } else {
                    name.setBackgroundColor(Color.parseColor("#f54242"));
                    sb.append(String.format("%s is not a valid monster, please select a valid monster.\n", monster));
                    isValid = false;
                    Log.d("TEST","Monster Quantity Invalid");
                }
            }
        }
    }

    private void checkMonsterQuantityValidity() {
        Log.d("TEST","Checking Monster Validity");
        int combatantCount = parentLinearLayout.getChildCount();
        View view;
        EditText num;
        EditText name;
        for (int i = 0; i < combatantCount; i++) {
            view = parentLinearLayout.getChildAt(i);
            if (view.getTag().toString().equals("monster_entry")) {
                name = view.findViewById(R.id.autoTxtViewMonsters);
                num = view.findViewById(R.id.editTxtMonsterNumber);
                try {
                    Integer.parseInt(num.getText().toString());
                    num.setBackgroundColor(Color.parseColor("#ffffff"));
                    Log.d("TEST","Monster Valid");
                } catch (NumberFormatException e) {
                    num.setBackgroundColor(Color.parseColor("#f54242"));
                    sb.append(String.format("%s must have a quantity entered.\n", name.getText().toString()));
                    isValid = false;
                    Log.d("TEST","Monster Invalid");
                }
            }
        }
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
}

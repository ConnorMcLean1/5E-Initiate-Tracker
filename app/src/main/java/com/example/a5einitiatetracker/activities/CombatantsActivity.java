package com.example.a5einitiatetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.api.APIUtility;
import com.example.a5einitiatetracker.api.json.JSONUtility;
import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.Monster;
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


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<   >(this,
                android.R.layout.simple_list_item_1, monsters);

        FloatingActionButton addMonsterButton = findViewById(R.id.fabAddMonster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.monster_entry_layout, null);
                AutoCompleteTextView autoCompleteTextView = rowView.findViewById(R.id.autoTxtViewMonsters);
                autoCompleteTextView.setAdapter(arrayAdapter);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        });

        FloatingActionButton addPlayerButton = findViewById(R.id.fabAddPlayer);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.player_entry_layout, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        combatantsList.clear();
    }

    public void startCombat(View v) {
        getPlayers();
        getMonsterData();
        //TODO: Add code to start new combat activity
    }

    // loads all the monster info and adds them to the combatant list, then sorts the list by
    // initiative
    private void getMonsterData() {
        final HashMap<String, Integer> monsters = createMonsterKeyValuePair();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String monsterIndex;
                String monsterName;
                for (HashMap.Entry<String, Integer> entry : monsters.entrySet()) {
                    monsterIndex = monsterNames.get(entry.getKey());
                    for (int i = 0; i < entry.getValue(); i++) {
                        NPC m = APIUtility.getMonsterByIndex(monsterIndex);
                        monsterName = String.format("%s %d", m.getName(), i+1);
                        m.setName(monsterName);
                        combatantsList.add(m);
                    }
                }

                Collections.sort(combatantsList);
            }
        }).start();
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

    // creates a hashmap of all the monsters with their name and the number to load
    private HashMap createMonsterKeyValuePair() {
        HashMap<String, Integer> m = new HashMap<String, Integer>();
        int combatantCount = parentLinearLayout.getChildCount();
        View view;
        AutoCompleteTextView name;
        EditText num;
        for (int i = 0; i < combatantCount; i++) {
            view = parentLinearLayout.getChildAt(i);
            if (!view.getTag().toString().equals("player_entry")) {
                num = view.findViewById(R.id.editTxtMonsterNumber);
                name = view.findViewById(R.id.autoTxtViewMonsters);
                m.put(
                        name.getText().toString(),
                        Integer.parseInt(num.getText().toString())
                );
            }
        }
        // for debugging
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            Log.v("MAP", String.format("Key: %s - Value: %s", entry.getKey(), entry.getValue()));
        }

        return m;
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

}
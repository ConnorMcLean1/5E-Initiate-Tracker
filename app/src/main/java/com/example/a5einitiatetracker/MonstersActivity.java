package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonstersActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    HashMap<String, String> monsterNames;
    public static List<Combatant> combatantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

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

        Button addMonsterButton = findViewById(R.id.btnAddMonster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.monster_entry_layout, null);
                AutoCompleteTextView autoCompleteTextView = rowView.findViewById(R.id.autoTxtViewMonsters);
                autoCompleteTextView.setAdapter(arrayAdapter);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        });
    }

    public void onGetMonsterData(View v) {
        final HashMap<String, Integer> monsters = createMonsterKeyValuePair();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String monsterIndex;
                for (HashMap.Entry<String, Integer> entry : monsters.entrySet()) {
                    Log.d("KEY", String.format("key: %s", entry.getKey()));
                    monsterIndex = monsterNames.get(entry.getKey());
                    Log.d("INDEX", String.format("index: %s", monsterIndex));
                    for (int i = 0; i < entry.getValue(); i++) {
                        combatantsList.add(APIUtility.getMonsterByIndex(monsterIndex));
                    }
                }

                String combatantData = "";

                for (Combatant c : combatantsList) {
                    combatantData += c.toString();
                }

                Log.d("COMBATANTS", "\n" + combatantData);
            }
        }).start();
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    private HashMap createMonsterKeyValuePair() {
        HashMap<String, Integer> m = new HashMap<String, Integer>();
        int monstersCount = parentLinearLayout.getChildCount();
        View view;
        AutoCompleteTextView name;
        EditText num;
        for (int i = 0; i < monstersCount; i++) {
            view = parentLinearLayout.getChildAt(i);
            num = view.findViewById(R.id.editTxtMonsterNumber);
            name = view.findViewById(R.id.autoTxtViewMonsters);
            m.put(
                    name.getText().toString(),
                    Integer.parseInt(num.getText().toString())
            );
        }
        // for debugging
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            Log.v("MAP", String.format("Key: %s - Value: %s", entry.getKey(), entry.getValue()));
        }

        return m;
    }

}

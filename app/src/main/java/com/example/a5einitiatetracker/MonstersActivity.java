package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Map;

public class MonstersActivity extends AppCompatActivity {
    private LinearLayout parentLinearLayout;
    HashMap<String, String> monsterNames;
    public static List<Monster> combatantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

        parentLinearLayout = findViewById(R.id.lnrLayoutMonsters);
        //Reads in the list of monster Names and Indexes from the JSON file created on startup
        monsterNames = JSONUtility.readMonsternamesFromJSONFile(this.getApplicationContext(), JSONUtility.JSON_FILE_NAME);

        String[] monsters = new String[monsterNames.size()];
        for (int i = 0; i < monsterNames.size(); i++) {
            monsters[i] = monsterNames.get(i).toString();
            Log.d("myTAG", monsters[i]);
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

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, String> monsters = new HashMap<Integer, String>();
                int monstersCount = parentLinearLayout.getChildCount();
                View view = null;
                AutoCompleteTextView name = null;
                EditText num = null;
                for (int i = 0; i < monstersCount; i++) {
                    view = parentLinearLayout.getChildAt(i);
                    num = view.findViewById(R.id.editTxtMonsterNumber);
                    name = view.findViewById(R.id.autoTxtViewMonsters);
                    monsters.put(
                            Integer.parseInt(num.getText().toString()),
                            name.getText().toString()
                    );
                }

                for (Map.Entry<Integer, String> entry : monsters.entrySet()) {
                    Log.v("MAP", String.format("Key: %s - Value: %s", entry.getKey(), entry.getValue()));
                }

            }
        });
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

}

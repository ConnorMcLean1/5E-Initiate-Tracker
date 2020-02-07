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
import android.widget.LinearLayout;

import java.io.File;
import java.util.List;

public class MonstersActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

        List<MonsterName> monsterNames = JSONUtility.readMonsternamesFromJSONFile(this.getApplicationContext(), MainActivity.JSON_FILE_NAME);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        String[] monsters = new String[MainActivity.monstersList.size()];
        for (int i = 0; i < MainActivity.monstersList.size(); i++) {
            monsters[i] = MainActivity.monstersList.get(i).toString();
            Log.d("myTAG", monsters[i]);
        }

//        AutoCompleteTextView actv = findViewById(R.id.actvMonsters);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<   >(this,
                android.R.layout.simple_list_item_1, monsters);

        Button addMonsterButton = findViewById(R.id.btnAddMonster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.monster_entry_layout, null);
                AutoCompleteTextView autoCompleteTextView = rowView.findViewById(R.id.actvMonsters);
                autoCompleteTextView.setAdapter(arrayAdapter);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        });
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
}

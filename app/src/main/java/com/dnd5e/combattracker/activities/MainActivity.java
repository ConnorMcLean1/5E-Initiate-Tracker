package com.dnd5e.combattracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dnd5e.combattracker.api.MonsterName;
import com.dnd5e.combattracker.R;
import com.dnd5e.combattracker.api.APIUtility;
import com.dnd5e.combattracker.api.json.JSONUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //variables for API using Retrofit library
    public static List<MonsterName> monstersList;

    File monsterListJSON;
    FileReader fr;
    BufferedReader br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File pointer to file storing the list of monster names and indexes from the API
        JSONUtility.createFile(this.getApplicationContext(), JSONUtility.JSON_FILE_NAME);
        monsterListJSON = new File(this.getFilesDir(), JSONUtility.JSON_FILE_NAME);

        try {
            fr = new FileReader(monsterListJSON);
            br = new BufferedReader(fr);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }


        ImageButton startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOnClick();
            }
        });

        //On create grabs the list of Monsters from the API
        APIUtility.connectAndGetApiData(this.getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startSavedCombat(View view) {
        File file = new File(this.getApplicationContext().getFilesDir(), JSONUtility.JSON_COMBAT_SAVED_FILE_NAME);
        if(file.exists()) {
            Intent intent = new Intent(getBaseContext(), CombatActivity.class);
            intent.putExtra("isSaved", true);
            startActivity(intent);
        }
        else{
            Toast.makeText(this.getApplicationContext(), "No saved combat exists to load!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startOnClick(){
        int temp = -1;
        try {
            temp = br.read();
            if(temp != -1) {
                Intent intent = new Intent(getBaseContext(), CombatantsActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this.getApplicationContext(), "We're sorry, an issue accessing the network occurred. Please ensure you are connected, wait a minute, and try again.", Toast.LENGTH_LONG).show();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
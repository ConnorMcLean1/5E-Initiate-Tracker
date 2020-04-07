package com.example.a5einitiatetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a5einitiatetracker.api.MonsterName;
import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.api.APIUtility;
import com.example.a5einitiatetracker.api.json.JSONUtility;
import com.google.gson.stream.JsonReader;

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
    JsonReader jr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File pointer to file storing the list of monster names and indexes from the API
        JSONUtility.createFile(this.getApplicationContext(), JSONUtility.JSON_FILE_NAME);
        monsterListJSON = new File(this.getFilesDir(), JSONUtility.JSON_FILE_NAME);

        try {
            fr = new FileReader(monsterListJSON);
            jr = new JsonReader(fr);
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
        try {
            if (!jr.hasNext()) {
                Toast.makeText(this.getApplicationContext(), "We're sorry, an issue accessing the network occurred. Please ensure you are connected, wait a minute, and try again.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getBaseContext(), CombatantsActivity.class);
                startActivity(intent);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
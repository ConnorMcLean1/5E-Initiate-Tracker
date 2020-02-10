package com.example.a5einitiatetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //variables for API using Retrofit library
    public static List<MonsterName> monstersList;

    File monsterListJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File pointer to file storing the list of monster names and indexes from the API
        JSONUtility.createFile(this.getApplicationContext(), JSONUtility.JSON_FILE_NAME);
        monsterListJSON = new File(this.getFilesDir(), JSONUtility.JSON_FILE_NAME);

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MonstersActivity.class);
                startActivity(intent);
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

}

package com.example.a5einitiatetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.a5einitiatetracker.api.MonsterName;
import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.api.APIUtility;
import com.example.a5einitiatetracker.api.json.JSONUtility;

import java.io.File;
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
                Intent intent = new Intent(getBaseContext(), CombatantsActivity.class);
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

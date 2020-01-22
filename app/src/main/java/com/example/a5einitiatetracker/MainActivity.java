package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DebugInfo";
    public static final String BASE_URL = "http://dnd5eapi.co/api/";
    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MonstersActivity.class);
                startActivity(intent);
            }
        });

        //On create grabs the list of Monsters from the API
        connectAndGetApiData();
    }

    //Method connects to the API and returns a list of MonsterName objects, each holding the index and name of a monster
    //TODO: monsterNames needs to be passed into a global variable or stored into a JSON file to be used by the app
    public void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIService APIService = retrofit.create(APIService.class);
        Call<MonsterIndex> call = APIService.listMonsters();
        call.enqueue(new Callback<MonsterIndex>() {
            @Override
            public void onResponse(Call<MonsterIndex> call, Response<MonsterIndex> response) {
                List<MonsterName> monsterNames = response.body().getResults();
            }

            @Override
            public void onFailure(Call<MonsterIndex> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}

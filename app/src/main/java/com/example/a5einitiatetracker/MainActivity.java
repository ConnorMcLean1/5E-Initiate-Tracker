package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //variables for API using Retrofit library
    public static final String BASE_API_URL = "http://dnd5eapi.co/api/";
    private static Retrofit retrofit = null;

    //JSON objects for storing monster list from the initial API call
    JSONArray jsonArray = new JSONArray();

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
    private void connectAndGetApiData() {
        List<MonsterName> monsterNames;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIService APIService = retrofit.create(APIService.class);
        Call<MonsterIndex> call = APIService.listMonsters();
        call.enqueue(new Callback<MonsterIndex>() {
            @Override
            public void onResponse(Call<MonsterIndex> call, Response<MonsterIndex> response) {
                List<MonsterName> monsterNames = response.body().getResults();
                MonsterNamesToJSONArray(monsterNames);
            }

            @Override
            public void onFailure(Call<MonsterIndex> call, Throwable throwable) {
                Log.e("API_RESPONSE", throwable.toString());
            }
        });
    }

    //Creates a JSON object out of each MonsterName object in the provided list
    //and adds it to a JSONArray.
    private JSONArray MonsterNamesToJSONArray(List<MonsterName> list){
        //region VARIABLES
        JSONArray arr = new JSONArray();
        JSONObject obj;
        //endregion
        try {
            //region LOOP
            for (int i = 0; i < list.size(); i++) {
                obj = new JSONObject();
                obj.put("Index", list.get(i).getIndex());
                obj.put("Name", list.get(i).getIndex());
                arr.put(obj);
            }
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting MonsterNames list to JSON Object: "
                    + e.getLocalizedMessage());
        }
        //endregion

        //region TESTING
        try {
            JSONObject tempOBJ;
            for (int i = 0; i < arr.length(); i++) {
                tempOBJ = arr.getJSONObject(i);
                Log.d("JSON_TEST", "Name: " + tempOBJ.getString("Name") + ", Index: "
                        + tempOBJ.getString("Index"));
            }
        }
        catch (Exception e){
            Log.e("JSON_TEST", "Error: " + e.getLocalizedMessage());
        }
        //endregion

        return arr;
    }
}

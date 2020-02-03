package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
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
    public static List<MonsterName> monstersList = null;

    private static final String JSON_FILE_NAME = "MonsterListJSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File pointer to file storing the list of monster names and indexes from the API
        File monsterListJSON = new File(JSON_FILE_NAME);

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
               storeMonstersToJSON(MonsterNamesToJSONArray(monsterNames));
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

    //Creates a new JSON file if one does not exist for storing the API Data and stores it as an
    //array of JSON objects
    private void storeMonstersToJSON(JSONArray arr){
        File file = new File(this.getFilesDir(), JSON_FILE_NAME);
        FileWriter fw;
        JsonWriter jw;

        //If the file does not exist, create a new one
        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch (Exception e){
                Log.e("FILE_CREATION", "Error: " + e.getLocalizedMessage());
            }
        }
        try{
            fw = new FileWriter(file.getAbsoluteFile());
            jw = new JsonWriter(fw);
            jw.beginArray();
            for(int i = 0; i < arr.length(); i++){
                jw.beginObject();
                jw.value(arr.getJSONObject(i).toString());
                jw.endObject();
            }
            jw.endArray();

        }
        catch (Exception e){
            Log.e("FILE_WRITING", "Error: " + e.getLocalizedMessage());
        }
    }
}

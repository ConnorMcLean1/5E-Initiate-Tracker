package com.example.a5einitiatetracker.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.a5einitiatetracker.api.json.JSONUtility;
import com.example.a5einitiatetracker.combatant.Monster;
import com.example.a5einitiatetracker.combatant.NPC;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtility {

    private static final String BASE_API_URL = "http://dnd5eapi.co/api/";
    private static Retrofit retrofit = null;
    private static  APIService APIService;

    //Note: MUST be called from an AsyncTask or other non-main UI thread or will throw an error
    //Grabs the data and creates an NPC object from the API based on the index of the monster given
    //Creates the NPC assuming they do NOT have advantage on their initiative roll, and they are in ALIVE state
    public static NPC getMonsterByIndex(String index){
        NPC npc = null;
        Monster monster;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIService = retrofit.create(APIService.class);
        Call<Monster> call = APIService.getMonsterStats(index);

        try {
            monster = call.execute().body();
            npc = new NPC(NPC.dexToMod(monster.getDex()), NPC.combatantStates.ALIVE, monster.getHp(), monster.getName(), 0, monster.getAc());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return npc;
    }

    //Performs the initial API call to grab the list of monster names and indexes
    //Retrieved data is stored to a JSON file for later use
    public static void connectAndGetApiData(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        final File file = new File(context.getFilesDir(), JSONUtility.JSON_FILE_NAME);

        APIService = retrofit.create(APIService.class);
        Call<MonsterIndex> call = APIService.listMonsters();
        call.enqueue(new Callback<MonsterIndex>() {
            @Override
            public void onResponse(@NonNull Call<MonsterIndex> call, @NonNull Response<MonsterIndex> response) {
                List<MonsterName> monsterNames = response.body().getResults();
                JSONUtility.storeMonstersToJSON(monsterNames, file);
            }

            @Override
            public void onFailure(@NonNull Call<MonsterIndex> call, @NonNull Throwable throwable) {
            }
        });
    }
}

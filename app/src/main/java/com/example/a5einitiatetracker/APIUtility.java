package com.example.a5einitiatetracker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtility {

    public static final String BASE_API_URL = "http://dnd5eapi.co/api/";
    private static Retrofit retrofit = null;


    /*public static Monster getMonsterByIndex(String index){

    }*/

   static void connectAndGetApiData(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        final File file = new File(context.getFilesDir(), JSONUtility.JSON_FILE_NAME);

        APIService APIService = retrofit.create(APIService.class);
        Call<MonsterIndex> call = APIService.listMonsters();
        call.enqueue(new Callback<MonsterIndex>() {
            @Override
            public void onResponse(@NonNull Call<MonsterIndex> call, @NonNull Response<MonsterIndex> response) {
                List<MonsterName> monsterNames = response.body().getResults();
                JSONUtility.storeMonstersToJSON(monsterNames, file);
            }

            @Override
            public void onFailure(@NonNull Call<MonsterIndex> call, @NonNull Throwable throwable) {
                Log.e("API_RESPONSE", throwable.toString());
            }
        });
    }
}

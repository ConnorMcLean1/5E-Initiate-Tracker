package com.example.a5einitiatetracker;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHandler {
    //the monster name is used for lookup of a particular monster from the API.
    // This API does not actually require an API key
    private final String API_KEY = "";
    NPC npc;

    //builder for retrofit using base URL
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    // create an instance of the ApiService
    private APIService apiService = retrofit.create(APIService.class);
    // make a request by calling the corresponding method
    private Single<Monster> monster;

     NPC lookupMonster(String name){
        monster = apiService.getMonsterData(name, API_KEY);

        //this part should be in an actual activity, not here
        monster.subscribe(new SingleObserver<Monster>() {
            @Override
            public void onSubscribe(Disposable d) {
                // we'll come back to this in a moment
            }

            @Override
            public void onSuccess(Monster monster) {
                int dex = monster.getDexterity();
                int hp = monster.getHit_points();
                String name = monster.getName();
                npc = new NPC(NPC.dexToMod(dex), Combatant.combatantStates.ALIVE, hp, 0, name);
            }
            @Override
            public void onError(Throwable e) {
                // oops, we best show some error message
            }
        });


        return npc;
     }

}



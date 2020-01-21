package com.example.a5einitiatetracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("monsters")
    Call<List<MonsterIndex>> listMonsters();
    @GET("monsters/{monster}")
    Call<Monster> getMonsterStats(@Path("monster") String monster);

}

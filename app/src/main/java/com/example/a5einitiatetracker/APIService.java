package com.example.a5einitiatetracker;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("monsters/{monster_name}")
    Single<Monster> getMonsterData(@Path("monster_name") String monsterName,
                                  @Query("api_key") String apiKey);
}


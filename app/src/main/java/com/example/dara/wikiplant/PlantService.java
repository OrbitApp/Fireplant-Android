package com.example.dara.wikiplant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pandawarrior91 on 30/04/2017.
 * Android Studio 2.2
 */

public interface PlantService {
    @GET("helloWorld")
    Call<String> getPlantKey(@Path("url") String url);
}

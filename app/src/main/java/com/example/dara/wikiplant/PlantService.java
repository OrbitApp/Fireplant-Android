package com.example.dara.wikiplant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pandawarrior91 on 30/04/2017.
 * Android Studio 2.2
 */

public interface PlantService {
    @POST("helloWorld")
    Call<ImageUriDownload> getPlantKey(@Body ImageUrlPayload url);
}

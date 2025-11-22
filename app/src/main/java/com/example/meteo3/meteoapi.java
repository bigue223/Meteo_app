package com.example.meteo3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface meteoapi {
    @GET("weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey
    );
}


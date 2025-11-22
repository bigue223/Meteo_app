package com.example.meteo3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText cityInput;
    TextView resultText;
    Button searchBtn;
    ImageView weatherIcon;

    String API_KEY = "0b9f75c4076e1344aa7de04a50f81484";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        resultText = findViewById(R.id.resultText);
        searchBtn = findViewById(R.id.searchBtn);
        weatherIcon = findViewById(R.id.weatherIcon);

        searchBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString();
            getWeather(city);
        });
    }

    private void getWeather(String city) {
        meteoapi api = RetrofitClient.getInstance().create(meteoapi.class);
        Call<WeatherResponse> call = api.getWeather(city, API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse w = response.body();

                    float tempC = w.main.temp - 273.15f;
                    String text =
                            "Temp: " + String.format("%.1f", tempC) + "°C\n" +
                                    "Humidity: " + w.main.humidity + "%\n" +
                                    "Sky: " + w.weather[0].description;

                    resultText.setText(text);

                    // تحميل أيقونة الطقس
                    String iconCode = w.weather[0].icon;
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    Glide.with(MainActivity.this)
                            .load(iconUrl)
                            .into(weatherIcon);

                } else {
                    resultText.setText("City not found!");
                    weatherIcon.setImageResource(0);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                resultText.setText("Error: " + t.getMessage());
                weatherIcon.setImageResource(0);
            }
        });
    }
}

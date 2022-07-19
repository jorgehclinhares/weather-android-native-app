package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    lateinit var temperatureText : TextView
    lateinit var weatherNameText : TextView
    lateinit var windSpeedText : TextView
    lateinit var humidityText : TextView
    lateinit var rainChancesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestWeatherInformation("Recife", "b2c11581905b4523a7203707221307")

        temperatureText = findViewById(R.id.temperature_text)
        weatherNameText = findViewById(R.id.weather_name_text)
        windSpeedText = findViewById(R.id.wind_speed_text)
        humidityText = findViewById(R.id.humidity_text)
        rainChancesText = findViewById(R.id.rain_chances_text)
    }

    private fun requestWeatherInformation(location: String, key: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.weatherapi.com/v1/current.json?key=${key}&q=${location}&aqi=no"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val current = response.getJSONObject("current")
                val condition = current.getJSONObject("condition")

                temperatureText.text = current.getString("temp_c") + "º"
                weatherNameText.text = condition.getString("text")
                windSpeedText.text = current.getString("wind_kph") + " km/h"
                humidityText.text = current.getString("humidity") + '%'
                rainChancesText.text = current.getString("pressure_in") + '%'
            },
            { error ->
                // TODO: Handle error
                Log.d("ERROR", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

}
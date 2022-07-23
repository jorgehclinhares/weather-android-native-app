package com.example.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    lateinit var temperatureText : TextView
    lateinit var weatherNameText : TextView
    lateinit var windSpeedText : TextView
    lateinit var humidityText : TextView
    lateinit var dataText: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestWeatherInformation("Recife", "b2c11581905b4523a7203707221307")

        temperatureText = findViewById(R.id.temperature_text)
        weatherNameText = findViewById(R.id.weather_name_text)
        windSpeedText = findViewById(R.id.wind_speed_text)
        humidityText = findViewById(R.id.humidity_text)
        dataText = findViewById(R.id.date_text)
        dataText.text = getDateFormatted()
    }

    private fun requestWeatherInformation(location: String, key: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.weatherapi.com/v1/current.json?key=${key}&q=${location}&aqi=no"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val current = response.getJSONObject("current")
                val condition = current.getJSONObject("condition")

                var temperatureFormated = current.getDouble("temp_c").toString().split('.')[0]
                var windSpeedInKMH =  current.getDouble("wind_kph").toString()
                var humidity = current.getString("humidity")

                temperatureText.text = "${temperatureFormated}º"
                weatherNameText.text = condition.getString("text")
                windSpeedText.text = "$windSpeedInKMH km/h"
                humidityText.text = "${humidity}%"
            },
            { error ->
                // TODO: Handle error
                Log.d("ERROR", error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateFormatted(): String? {
        val current = LocalDateTime.now()
        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE")
        var dayOfWeek = current.format(dayOfWeekFormatter)
        dayOfWeek = dayOfWeek.capitalize()

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
        var date = current.format(dateFormatter)
        date = date.capitalize()

        return "${dayOfWeek}, $date"

    }

}
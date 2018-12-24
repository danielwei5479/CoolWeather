package com.msi.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getString("weather_id",null) != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id",pref.getString("weather_id",null));
            startActivity(intent);
            finish();
        }
    }
}

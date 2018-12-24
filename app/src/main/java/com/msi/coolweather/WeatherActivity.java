package com.msi.coolweather;

import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msi.coolweather.gson.Weather.Forecast;
import com.msi.coolweather.gson.Weather.Lifestyle;
import com.msi.coolweather.gson.Weather.Weather;
import com.msi.coolweather.gson.air.Air;
import com.msi.coolweather.util.HttpUtil;
import com.msi.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by MSI on 18/12/21.
 */

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    //内容显示
    private Weather weather;
    private Air     air;
    private ImageView bingPicImg;
    //修复一些BUG
    //修复一些BUG
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_weather);
        initWidget();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //调出之前保存的数据
        String weatherString = prefs.getString("weather",null);    //天气数据
        String airString     = prefs.getString("air",null);        //空气数据
        String bingPic       = prefs.getString("pic",null);        //图片数据
        if(weatherString != null  && air != null ){
            //有缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            Air air =Utility.handleAirResponse(airString);
            showWeatherInfo(weather,air);
            //setAir(air);
            //setWeather(weather);
            //showWeatherInfo();
        }else {
            //没缓存去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            //保存查询地址的ID
            editor.putString("weather_id",weatherId);
            editor.apply();
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather_Air(weatherId);
            //调出之前保存的数据
            weatherString = prefs.getString("weather",null);
            airString     = prefs.getString("air",null);
            Weather weather = Utility.handleWeatherResponse(weatherString);
            Air     air     = Utility.handleAirResponse(airString);
            showWeatherInfo(weather,air);
        }
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载图片失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void requestWeather_Air(final String weatherId) {

        String weatherUrl = "https://free-api.heweather.net/s6/weather?location="+weatherId+"&key=b6b178afc63a4f8da7ab895502a94cd9";
        String airUrl = "https://free-api.heweather.net/s6/air/now?location=CN101010100&key=b6b178afc63a4f8da7ab895502a94cd9";
        //更新天气信息，并生成Weather对象
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText  = response.body().string();
                if(responseText != null){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("weather", responseText);
                    editor.apply();
                }
            }
        });
        //更新AQI并生成Air对象
        HttpUtil.sendOkHttpRequest(airUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取AQI失败,连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                if (responseText != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("air", responseText);
                    editor.apply();
                }
            }
        });
    }

    private void initWidget(){
        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);

        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);

    }
    /**
     * 处理并展示Weather实体类信息
     */
    private void showWeatherInfo(Weather weather,Air air){
        String cityName = weather.basic.location;
        String updateTime = weather.update.loc;
        String degerss  = weather.now.tmp;
        String weatherInfo = weather.now.cond_txt;

        titleCity.setText(cityName);    //设置城市名称
        titleUpdateTime.setText("更新时间:"+updateTime.substring(5));
        degreeText.setText(degerss+"℃");
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.daily_forecast){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText("日:"+forecast.cond_txt_d+"  "+"夜:"+forecast.cond_txt_n);
            maxText.setText(forecast.tmp_max);
            minText.setText(forecast.tmp_min);
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
        //设置空气质量
        //1.设置AQI指数
        aqiText.setText(air.air_now_city.aqi);
        //PM2.5指数
        pm25Text.setText(air.air_now_city.pm25);

        //设置生活建议
        for(Lifestyle lifestyle:weather.lifestyle){
            if(lifestyle.type.equals("comf")){
                comfortText.setText("舒适度指数:"+lifestyle.brf+"。"+lifestyle.txt);
            }
            if(lifestyle.type.equals("cw")){
                carWashText.setText("洗车指数:"+lifestyle.brf+"。"+lifestyle.txt);
            }
            if(lifestyle.type.equals("sport")){
                sportText.setText("运动指数:"+lifestyle.brf+"。"+lifestyle.txt);
            }
        }
    }
    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather tmp) {
        weather = tmp;
    }

    public Air getAir() {
        return air;
    }

    public void setAir(Air tmp) {
        air = tmp;
    }
}

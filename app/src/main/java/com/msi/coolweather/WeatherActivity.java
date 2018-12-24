package com.msi.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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


    public SwipeRefreshLayout swipeRefresh;  //上拉刷新

    private ScrollView weatherLayout;
    private TextView titleCity;            //城市名称
    private TextView titleUpdateTime;      //更新时间
    private TextView degreeText;           //当前气温
    private TextView weatherInfoText;      //天气信息
    private LinearLayout forecastLayout;   //未来7天天气预报
    private TextView aqiText;              //空气信息
    private TextView pm25Text;
    private TextView comfortText;           //建议
    private TextView carWashText;           //建议
    private TextView sportText;             //建议

    //内容显示
    private Weather weather;
    private Air     air;
    private ImageView bingPicImg;
    //修复一些BUG
    //修复一些BUG
    private boolean updateFlag = false;

    //切换城市
    public DrawerLayout drawerLayout;
    private Button navButton;


    //天气数据ID
    private String weatherId;
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_weather);StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initWidget();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = prefs.getString("weather",null);    //天气数据
        String airString     = prefs.getString("air",null);        //空气数据
        String bingPic       = prefs.getString("pic",null);        //图片数据
        if(weatherString != null  && airString != null ){
            //有缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            Air air =Utility.handleAirResponse(airString);
            weatherId = weather.basic.cid;
            showWeatherInfo(weather,air);
        }else {
            //没缓存去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            editor.putString("weather_id",weatherId);
            editor.apply();
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather_Air(weatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather_Air(weatherId);
                Toast.makeText(WeatherActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
            }
        });
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

    public boolean requestWeather_Air(final String weatherId) {
        boolean isResponseAlready = false;
        String weatherUrl = "https://free-api.heweather.net/s6/weather?location="+weatherId+"&key=b6b178afc63a4f8da7ab895502a94cd9";
        String airUrl = "https://free-api.heweather.net/s6/air/now?location="+weatherId+"&key=b6b178afc63a4f8da7ab895502a94cd9";
        //更新天气信息，并生成Weather对象
        try{
            final String message = HttpUtil.syncSendOkHttpRequest(weatherUrl);
            if(message != null ){
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("weather", message);
                editor.apply();
            }else{
                Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //获取空气信息
        try{
            final String message = HttpUtil.syncSendOkHttpRequest(airUrl);
            if(message != null ){
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("air", message);
                editor.apply();
            }else{
                Toast.makeText(WeatherActivity.this,"获取空气数据失败",Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //调出保存的数据
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        Log.d("aa","调出保存的数据"+prefs.getString("weather",null));
        final Weather weather = Utility.handleWeatherResponse(prefs.getString("weather",null));
        final Air     air     = Utility.handleAirResponse(prefs.getString("air",null));
        showWeatherInfo(weather,air);
        swipeRefresh.setRefreshing(false);
        return true;
    }

    private void initWidget(){
        // 初始化各控件
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
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

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    /**
     * 处理并展示Weather实体类信息
     */
    private void showWeatherInfo(Weather weather,Air air){
        //启动8小时后的定时服务
        if(weather != null && "ok".equals(weather.status)){
            Intent intent = new Intent(this,AutoUpdateService.class);
        }else{
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }

        Log.d("aa","showWeatherInfo");
        Log.d("aa","showWeatherInfo" + weather.basic.location);
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
        if(air.status.equals("ok")){
            //1.设置AQI指数
            aqiText.setText(air.air_now_city.aqi);
            //PM2.5指数
            pm25Text.setText(air.air_now_city.pm25);
        }else{
            //1.设置AQI指数
            aqiText.setTextSize(20);
            aqiText.setText("暂无数据");
            //PM2.5指数
            pm25Text.setTextSize(20);
            pm25Text.setText("暂无数据");
        }
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
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherId() {
        return weatherId;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

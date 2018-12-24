package com.msi.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.msi.coolweather.db.City;
import com.msi.coolweather.db.County;
import com.msi.coolweather.db.Province;
import com.msi.coolweather.gson.Weather.Weather;
import com.msi.coolweather.gson.air.Air;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MSI on 18/12/21.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response)  {
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince = new JSONArray(response);
                for(int i = 0 ; i < allProvince.length();i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;
    }
    /**
     * 解析和处理市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCity = new JSONArray(response);
                for(int i = 0 ;i<allCity.length();i++){
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        Log.d("handleCityResponse","handleCityResponse"+":  " + response);
        return false;

    }
    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounty = new JSONArray(response);
                for(int i = 0;i<allCounty.length();i++){
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                Log.d("handleCountyResponse","handleCountyResponse"+":  " + response);
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Log.d("aa","获取到文本...//weatherContent:"+weatherContent);
            return new Gson().fromJson(weatherContent,Weather.class);
            //return weather;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将返回的JSON数据解析成Air实体类
     */
    public static Air handleAirResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String airContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(airContent,Air.class);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}

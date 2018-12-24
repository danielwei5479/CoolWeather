package com.msi.coolweather.gson.Weather;

import com.msi.coolweather.gson.Basic;
import com.msi.coolweather.gson.Update;

import java.util.List;

/**
 * Created by MSI on 18/12/21.
 */

public class Weather {
    public Basic basic;
    public List<Forecast> daily_forecast;
    public List<Hourly> hourly;
    public List<Lifestyle> lifestyle;
    public Now now;
    public String status;
    public Update update;
}

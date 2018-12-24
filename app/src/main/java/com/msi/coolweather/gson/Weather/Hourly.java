package com.msi.coolweather.gson.Weather;

/**
 * Created by MSI on 18/12/21.
 */

public class Hourly {
    public String time;   //	预报时间，格式yyyy-MM-dd hh:mm	2013-12-30 13:00
    public String tmp;   //	温度	2
    public String cond_code;   //	天气状况代码	101
    public String cond_txt;   //	天气状况代码	多云
    public String wind_deg;   //	风向360角度	290
    public String wind_dir;   //	风向	西北
    public String wind_sc;   //	风力	3-4
    public String wind_spd;   //	风速，公里/小时	15
    public String hum;   //	相对湿度	30
    public String pres;   //	大气压强	1030
    public String dew;   //	露点温度	12
    public String cloud;   //	云量	23
}

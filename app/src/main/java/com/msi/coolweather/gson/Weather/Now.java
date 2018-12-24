package com.msi.coolweather.gson.Weather;

/**
 * Created by MSI on 18/12/21.
 */

public class Now {
    public String fl;   //	体感温度，默认单位：摄氏度	23
    public String tmp;//	温度，默认单位：摄氏度	21
    public String cond_code;//	实况天气状况代码	100
    public String  cond_txt;//	实况天气状况描述	晴
    public String wind_deg;  //	风向360角度	305
    public String wind_dir;  //	风向	西北
    public String wind_sc;  //	风力	3-4
    public String wind_spd;  //	风速，公里/小时	15
    public String hum;  //	相对湿度	40
    public String pcpn;  //	降水量	0
    public String pres;  //	大气压强	1020
    public String vis;  //	能见度，默认单位：公里	10
    public String cloud;  //	云量	23
}

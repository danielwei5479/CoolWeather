package com.msi.coolweather.gson.air;

/**
 * Created by MSI on 18/12/23.
 */

/**
 * 空气质量 AQI 逐小时预报
 */
public class AirHourly {
    public String time;           //	预报日期，格式yyyy-MM-dd HH:mm	2017-08-09 14:00
    public String aqi;            //	空气质量指数，AQI和PM25的关系	74
    public String main;           //	主要污染物	pm25
    public String qlty;           //	空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染，查看计算方式	良
}

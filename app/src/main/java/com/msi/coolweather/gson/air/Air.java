package com.msi.coolweather.gson.air;

/**
 * Created by MSI on 18/12/23.
 */

import com.msi.coolweather.gson.Basic;
import com.msi.coolweather.gson.Update;

import java.util.List;

/**
 * 空气质量实况
 */
public class Air {

    public String status;                      //状态码

    public Basic basic;                        //基础信息

    public Update update;                      //接口更新时间

    public AirNowCity air_now_city;              //AQI城市实况

    public List<AirNowStation> air_now_station; //AQI站点实况

}

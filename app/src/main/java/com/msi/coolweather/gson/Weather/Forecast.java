package com.msi.coolweather.gson.Weather;

/**
 * Created by MSI on 18/12/21.
 */

public class Forecast {
    public String date;//	预报日期	2013-12-30
    public String sr;//	日出时间	07:36
    public String ss;//	日落时间	16:58
    public String mr;//	月升时间	04:47
    public String ms;//	月落时间	14:59
    public String tmp_max;//	最高温度	4
    public String tmp_min;//	最低温度	-5
    public String cond_code_d;//	白天天气状况代码	100
    public String cond_code_n;//	晚间天气状况代码	100
    public String cond_txt_d;//	白天天气状况描述	晴
    public String cond_txt_n;//	晚间天气状况描述	晴
    public String wind_deg;//	风向360角度	310
    public String  wind_dir;//	风向	西北风
    public String wind_sc;//	风力	1-2
    public String wind_spd;//	风速，公里/小时	14
    public String hum;//	相对湿度	37
    public String pcpn;//	降水量	0
    public String pop;//	降水概率	0
    public String pres;//	大气压强	1018
    public String uv_index;//	紫外线强度指数	3
    public String vis;//	能见度，单位：公里	10
}

package com.msi.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MSI on 18/12/21.
 */

public class Basic {

    public String location;     //	地区/城市名称  如:北京
    public String cid;    //城市ID   如：CN101080402
    public String lat;    //城市纬度
    public String lon;    //城市经度
    public String parent_city;  //该地区/城市的上级城市 如：乌兰察布
    public String admin_area;   //城市所属行政区域 如：内蒙古
    public String tz;           //城市所在时区
}
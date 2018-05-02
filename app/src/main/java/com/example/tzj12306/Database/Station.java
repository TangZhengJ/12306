package com.example.tzj12306.Database;

import org.litepal.crud.DataSupport;

/**
 * Created by 正军 on 2018/4/2.
 */

public class Station extends DataSupport{
    int id;
    String city_name;
    String station_name;
    String abbreviate;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getAbbreviate() {
        return abbreviate;
    }

    public void setAbbreviate(String abbreviate) {
        this.abbreviate = abbreviate;
    }
}

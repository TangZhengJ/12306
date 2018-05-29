package com.example.tzj12306.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by 正军 on 2018/5/3.
 */

public class County extends DataSupport implements Parcelable{
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countyName);
        dest.writeString(weatherId);
    }

    public static final Parcelable.Creator<County> CREATOR = new Parcelable.Creator<County>(){
        @Override
        public County createFromParcel(Parcel in) {
            County county = new County();

            county.countyName = in.readString();
            county.weatherId = in.readString();

            return county;
        }

        @Override
        public County[] newArray(int size) {
            return new County[size];
        }
    };
}

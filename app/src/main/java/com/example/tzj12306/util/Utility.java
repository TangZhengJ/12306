package com.example.tzj12306.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.tzj12306.db.City;
import com.example.tzj12306.db.County;
import com.example.tzj12306.db.IdCard;
import com.example.tzj12306.db.Province;
import com.example.tzj12306.db.User;
import com.example.tzj12306.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 正军 on 2018/5/3.
 */

public class Utility {
    static String TAG = "Utility";
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i = 0; i < allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for(int i = 0; i < allCounties.length(); i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);;
                    county.save();
                }
                return true;
            } catch (JSONException e) {
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
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 解析登录用户信息
     */
    public static User parseUserXML(String response){
        Log.d("22", "onFailure: "+response);
        User user = new User();
        IdCard idCard = new IdCard();
        List<IdCard> cards = new ArrayList<IdCard>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("fail".equals(nodeName)){
                            user.setLoginFlag(false);
                            break;
                        } else if("success".equals(nodeName)){
                            user.setLoginFlag(true);
                        }else if("UserId".equals(nodeName)){
                            user.setUserName(xmlPullParser.nextText());
                        }else if("Password".equals(nodeName)){
                            user.setPassword(xmlPullParser.nextText());
                        }else if("Email".equals(nodeName)){
                            user.setEmail(xmlPullParser.nextText());
                        }else if("PhoneNum".equals(nodeName)){
                            user.setPhoneNum(xmlPullParser.nextText());
                        }else if("IdCard".equals(nodeName)){
                            idCard = new IdCard();
                        }else if("CardId".equals(nodeName)){
                            idCard.setCardId(xmlPullParser.nextText());
                        }else if("UserName".equals(nodeName)){
                            idCard.setName(xmlPullParser.nextText());
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("IdCard".equals(nodeName)){
                            cards.add(idCard);
                        }else if("USER".equals(nodeName)){
                            user.setIdCards(cards);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;

    }
    /**
     * 解析注册用户信息
     */
    public static boolean parseLoginResponseXML(String response){
        Log.d(TAG, "parseLoginResponseXML: " +response);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:{
                        if("fail".equals(nodeName)){
                            return false;
                        } else if("success".equals(nodeName)){
                            return true;
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}

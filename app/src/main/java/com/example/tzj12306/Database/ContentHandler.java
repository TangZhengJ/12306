package com.example.tzj12306.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.litepal.LitePal;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 正军 on 2018/4/20.
 *
 */

public class ContentHandler extends DefaultHandler{
    private String nodeName;
    private StringBuilder id;
    private StringBuilder abbreviate;
    private StringBuilder city_name;
    private StringBuilder station_name;
    private List<Station> stations;
    private StringBuilder sql;
    private SQLiteDatabase db;
    @Override
    public void startDocument() throws SAXException {
        Log.d("startDocument","startDocument");
        id = new StringBuilder();
        abbreviate = new StringBuilder();
        city_name = new StringBuilder();
        station_name = new StringBuilder();
        stations = new ArrayList<Station>();
        sql = new StringBuilder();
        db = LitePal.getDatabase();
        db.beginTransaction();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if("id".equals(nodeName)){
            id.append(ch,start,length);
        }else if("abbreviate".equals(nodeName)){
            abbreviate.append(ch,start,length);
        }else if("city_name".equals(nodeName)){
            city_name.append(ch,start,length);
        }else if("station_name".equals(nodeName)){
            station_name.append(ch,start,length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("station".equals(localName)){
            sql.append("INSERT INTO `station` VALUES (").append(id.toString().trim()).append(",'").append(abbreviate.toString().trim()).append("','")
                    .append(city_name.toString().trim()).append("','").append(station_name.toString().trim()).append("')");
            Log.d("endElement",sql.toString().trim());
            db.execSQL(sql.toString().trim());

            Station station = new Station();
            station.setAbbreviate(abbreviate.toString().trim());
            station.setId(Integer.parseInt(id.toString().trim()));
            station.setCity_name(city_name.toString().trim());
            station.setStation_name(station_name.toString().trim());
            stations.add(station);
            id.setLength(0);
            abbreviate.setLength(0);
            city_name.setLength(0);
            station_name.setLength(0);
            sql.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        //设置事务标志为成功，当结束事务时就会提交事务
        db.setTransactionSuccessful();
        super.endDocument();
        db.endTransaction();
    }

    public List<Station> getStations() {
        return stations;
    }
}

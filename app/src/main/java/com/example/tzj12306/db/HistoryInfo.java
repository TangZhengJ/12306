package com.example.tzj12306.db;

public class HistoryInfo {
    private String history_start;
    private String history_end;
    private String weatherId_start;
    private String weatherId_end;
    public HistoryInfo(String history_start, String history_end) {
    }

    public String getWeatherId_start() {
        return weatherId_start;
    }

    public void setWeatherId_start(String weatherId_start) {
        this.weatherId_start = weatherId_start;
    }

    public String getWeatherId_end() {
        return weatherId_end;
    }

    public void setWeatherId_end(String weatherId_end) {
        this.weatherId_end = weatherId_end;
    }

    public String getHistory_start() {
        return history_start;
    }

    public void setHistory_start(String history_start) {
        this.history_start = history_start;
    }

    public String getHistory_end() {
        return history_end;
    }

    public void setHistory_end(String history_end) {
        this.history_end = history_end;
    }
}

package com.example.tzj12306.db;

public class HistoryInfo {
    private String history_start;
    private String history_end;

    public HistoryInfo(String history_start, String history_end) {
        this.history_start = history_start;
        this.history_end = history_end;
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

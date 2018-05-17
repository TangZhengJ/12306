package com.example.tzj12306.db;

import android.graphics.Bitmap;

/**
 * Created by 正军 on 2018/3/24.
 */

public class TrianInfo {
    private String start;
    private String end;
    private int num;
    private Bitmap bitmap_start;
    private Bitmap bitmap_end;

    public TrianInfo(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Bitmap getBitmap_start() {
        return bitmap_start;
    }

    public void setBitmap_start(Bitmap bitmap_start) {
        this.bitmap_start = bitmap_start;
    }

    public Bitmap getBitmap_end() {
        return bitmap_end;
    }

    public void setBitmap_end(Bitmap bitmap_end) {
        this.bitmap_end = bitmap_end;
    }
}

package com.example.tzj12306.Station;

import android.os.AsyncTask;

/**
 * Created by 正军 on 2018/4/24.
 */

public class DownLoadTask extends AsyncTask<Void,Integer,Boolean>{
    //任务开始前调用
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    //处理所有耗时操作，在子线程中运行
    @Override
    protected Boolean doInBackground(Void... params) {
        return null;

    }
    //publishProgress()调用，更新UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
    //通过return语句进行返回时调用
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}

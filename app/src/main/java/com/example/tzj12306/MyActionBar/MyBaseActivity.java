package com.example.tzj12306.MyActionBar;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tzj12306.R;
import com.example.tzj12306.impl.ActionBarClickListener;

public abstract class MyBaseActivity extends AppCompatActivity {
    private MyActionbar myActionbar;
    protected abstract int getContentViewId();
    protected abstract void init();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(getContentViewId());
        init();
    }
    protected void setMyActionBar(int id, String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight, final ActionBarClickListener listener) {
        myActionbar = (MyActionbar) findViewById(id);
        myActionbar.setData(strTitle, resIdLeft, strLeft, resIdRight, strRight, listener);
    }
    /**
     * 获取actionBar
     *
     * @return
     */
    protected MyActionbar getMyActionBar() {
        return myActionbar;
    }
}

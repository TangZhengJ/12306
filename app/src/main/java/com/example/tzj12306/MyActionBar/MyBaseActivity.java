package com.example.tzj12306.MyActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tzj12306.R;
import com.example.tzj12306.impl.ActionBarClickListener;

public abstract class MyBaseActivity extends AppCompatActivity {
    private MyActionbar myActionbar;
    protected abstract int getContentViewId();
    protected abstract void init();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        init();
    }
    protected void setMyActionBar(String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight, final ActionBarClickListener listener) {
        myActionbar = (MyActionbar) findViewById(R.id.actionbar);
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

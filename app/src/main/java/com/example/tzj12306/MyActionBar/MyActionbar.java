package com.example.tzj12306.MyActionBar;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tzj12306.R;
import com.example.tzj12306.impl.ActionBarClickListener;

public final class MyActionbar extends LinearLayout {
    private View layRoot;
    private View myStatusBar;
    private View layLeft;
    private View layRight;
    private Button btTitle;
    private TextView tvLeft;
    private TextView tvRight;
    private View iconLeft;
    private View iconRight;
    public MyActionbar(Context context){
        this(context,null);
    }
    public MyActionbar(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    public MyActionbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){
        setOrientation(HORIZONTAL);
        View contentView = inflate(getContext(), R.layout.my_actionbar,this);
        layRoot = contentView.findViewById(R.id.lay_transroot);
        myStatusBar = contentView.findViewById(R.id.my_statusbar);
        btTitle = contentView.findViewById(R.id.tv_actionbar_title);
        tvLeft = contentView.findViewById(R.id.tv_actionbar_left);
        tvRight = contentView.findViewById(R.id.tv_actionbar_right);
        iconLeft = contentView.findViewById(R.id.iv_actionbar_left);
        iconRight = contentView.findViewById(R.id.iv_actionbar_right);
    }
    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        ViewGroup.LayoutParams params = myStatusBar.getLayoutParams();
        params.height = statusBarHeight;
        myStatusBar.setLayoutParams(params);
    }
    /**
     * 设置是否需要渐变
     *
     * @param translucent
     */
    public void setNeedTranslucent(boolean translucent) {
        if (translucent) {
            layRoot.setBackgroundDrawable(null);
        }
    }

    /**
     * 设置标题
     *
     * @param strTitle
     */
    public void setTitle(String strTitle) {
        if (!TextUtils.isEmpty(strTitle)) {
            btTitle.setText(strTitle);
            btTitle.setVisibility(View.VISIBLE);
        } else {
            btTitle.setVisibility(View.GONE);
        }
    }
    /**
     * 设置透明度
     *
     * @param transAlpha 0-255 之间
     */
    public void setTranslucent(int transAlpha) {
        layRoot.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.primary), transAlpha));
        btTitle.setAlpha(transAlpha);
        tvLeft.setAlpha(transAlpha);
        tvRight.setAlpha(transAlpha);
        iconLeft.setAlpha(transAlpha);
        iconRight.setAlpha(transAlpha);
    }
    /**
     * 设置数据
     *
     * @param strTitle
     * @param resIdLeft
     * @param strLeft
     * @param resIdRight
     * @param strRight
     * @param listener
     */
    public void setData(String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight, final ActionBarClickListener listener) {
        if (!TextUtils.isEmpty(strTitle)) {
            btTitle.setText(strTitle);
        } else {
            btTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strLeft)) {
            tvLeft.setText(strLeft);
            tvLeft.setVisibility(View.VISIBLE);
        } else {
            tvLeft.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(strRight)) {
            tvRight.setText(strRight);
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }

        if (resIdLeft == 0) {
            iconLeft.setVisibility(View.GONE);
        } else {
            iconLeft.setBackgroundResource(resIdLeft);
            iconLeft.setVisibility(View.VISIBLE);
        }

        if (resIdRight == 0) {
            iconRight.setVisibility(View.GONE);
        } else {
            iconRight.setBackgroundResource(resIdRight);
            iconRight.setVisibility(View.VISIBLE);
        }

        if (listener != null) {
            layLeft = findViewById(R.id.lay_actionbar_left);
            layRight = findViewById(R.id.lay_actionbar_right);
            layLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            layRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }
}

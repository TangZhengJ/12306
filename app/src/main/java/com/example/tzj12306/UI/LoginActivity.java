package com.example.tzj12306.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;

public class LoginActivity extends MyBaseActivity {
    CheckedTextView ctv_autologin;
    CheckedTextView ctv_remember;
    EditText tv_login_id;
    EditText tv_login_password;
    Button button_login;
    Button button_register;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        button_login = (Button)findViewById(R.id.button_login);
        Button title = (Button)findViewById(R.id.tv_actionbar_title);
        tv_login_id = (EditText) findViewById(R.id.et_login_id);
        tv_login_password = (EditText) findViewById(R.id.et_login_password);
        button_register = (Button) findViewById(R.id.button_register) ;
        title.setText("登录12306");
        ctv_remember = (CheckedTextView)findViewById(R.id.ctv_login_remember);
        ctv_autologin = (CheckedTextView)findViewById(R.id.ctv_login_autologin);
        ctv_autologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctv_autologin.setChecked(!ctv_autologin.isChecked());
            }
        });
        ctv_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctv_remember.setChecked(!ctv_remember.isChecked());
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean remember,autologin;
                remember = ctv_remember.isChecked();
                autologin = ctv_autologin.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                editor.putString("ID",tv_login_id.getText().toString());
                editor.putString("PSW",tv_login_password.getText().toString());
                editor.putBoolean("REM",remember);
                editor.putBoolean("AUTO",autologin);
                editor.apply();
                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
        if(pref.getBoolean("REM",false)) {
            tv_login_id.setText(pref.getString("ID", ""));
            tv_login_password.setText(pref.getString("PSW", ""));
            ctv_remember.setChecked(pref.getBoolean("REM",false));
            ctv_autologin.setChecked(pref.getBoolean("AUTO",false));
            if (ctv_autologin.isChecked()) {
                button_login.performClick();
            }
        }
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putBoolean("AUTO",false);
        editor.apply();
        ctv_autologin.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if(resultCode == RESULT_OK){
                    tv_login_id.setText(data.getStringExtra("user_name"));
                    tv_login_password.setText(data.getStringExtra("password"));
                    break;
                }
            }
        }
    }
}


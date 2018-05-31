package com.example.tzj12306.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.db.User;
import com.example.tzj12306.util.Encrypt;
import com.example.tzj12306.util.HttpUtil;
import com.example.tzj12306.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);

        if(pref.contains("REM")) {
            tv_login_id.setText(pref.getString("ID", ""));
            tv_login_password.setText(pref.getString("PSW", ""));
            ctv_remember.setChecked(pref.getBoolean("REM",false));
            ctv_autologin.setChecked(pref.getBoolean("AUTO",false));
            if (ctv_autologin.isChecked()) {
                button_login.performClick();
            }
        }
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
                String id = tv_login_id.getText().toString();
                String password = tv_login_password.getText().toString();
                if(remember){
                    autologin = ctv_autologin.isChecked();
                    SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                    editor.putString("ID",id);
                    editor.putString("PSW",password);
                    editor.putBoolean("REM",remember);
                    editor.putBoolean("AUTO",autologin);
                    editor.apply();
                }
                UserLogin(id,password);

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

    private void UserLogin(String userId,String password) {
        password = Encrypt.md5(password);
        HttpUtil.sendOkHttpRequest("http://192.168.1.130:8080/12306/User_Login.jsp?userId="+userId+"&password="+password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String ex = e.toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final User user =  Utility.parseUserXML(response.body().string());
                Log.d("11", "onFailure: "+user.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(user.isLoginFlag()){

                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("user",user);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
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


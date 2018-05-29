package com.example.tzj12306.UI;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.db.User;
import com.example.tzj12306.impl.ActionBarClickListener;

import org.litepal.crud.DataSupport;

public class RegisterActivity extends MyBaseActivity implements ActionBarClickListener{
    private EditText register_id;
    private EditText register_ack_password;
    private EditText register_email;
    private EditText register_name;
    private EditText register_password;
    private EditText register_phone_num;
    private EditText register_user_name;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        register_id = (EditText) findViewById(R.id.register_id);
        register_ack_password = (EditText) findViewById(R.id.register_ack_password);
        register_email = (EditText) findViewById(R.id.register_email);
        register_name = (EditText) findViewById(R.id.register_name);
        register_password = (EditText) findViewById(R.id.register_password);
        register_phone_num = (EditText) findViewById(R.id.register_phone_num);
        register_user_name = (EditText) findViewById(R.id.register_user_name);
        setMyActionBar(R.id.actionbar_register,"注册", R.mipmap.ic_left_light, "返回", R.mipmap.ic_right_light, "确认", this);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onCenterClick() {

    }

    @Override
    public void onRightClick() {
        String id;
        String ack_password;
        String email;
        String name;
        String password;
        String phone_num;
        String user_name;
        id = String.valueOf(register_id.getText());
        ack_password = String.valueOf(register_ack_password.getText());
        email = String.valueOf(register_email.getText());
        name = String.valueOf(register_name.getText());
        password = String.valueOf(register_password.getText());
        phone_num = String.valueOf(register_phone_num.getText());
        user_name = String.valueOf(register_user_name.getText());
        if((DataSupport.where("UserName = ?",user_name).find(User.class)).size()!=0){
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!ack_password.equals(password))
        {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        }else {
            User user = new User();
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            user.save();
            Intent intent = new Intent();
            intent.putExtra("user_name",user_name);
            intent.putExtra("password",password);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}

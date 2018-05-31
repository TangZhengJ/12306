package com.example.tzj12306.UI;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.db.User;
import com.example.tzj12306.impl.ActionBarClickListener;
import com.example.tzj12306.util.Encrypt;
import com.example.tzj12306.util.HttpUtil;
import com.example.tzj12306.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends MyBaseActivity implements ActionBarClickListener{
    private EditText register_idcard;
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
        register_idcard = (EditText) findViewById(R.id.register_idcard);
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
        String idCard;
        String ack_password;
        String email;
        String name;
        final String password;
        String phone_num;
        final String user_name;
        idCard = String.valueOf(register_idcard.getText());
        ack_password = String.valueOf(register_ack_password.getText());
        email = String.valueOf(register_email.getText());
        name = String.valueOf(register_name.getText());
        password = String.valueOf(register_password.getText());
        phone_num = String.valueOf(register_phone_num.getText());
        user_name = String.valueOf(register_user_name.getText());
//        if((DataSupport.where("UserName = ?",user_name).find(User.class)).size()!=0){
//            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
//        }else
        if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!ack_password.equals(password))
        {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        }else if(!isMobileNO(phone_num)){
            Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
        } else if(!isEmail(email)){
            Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
        }
        else {
            RequestBody requestBody = new FormBody.Builder()
                    .add("UserId",user_name)
                    .add("Password", Encrypt.md5(password))
                    .add("PhoneNum",phone_num)
                    .add("Email",email)
                    .add("Name",name)
                    .add("CardId",idCard)
                    .build();
            HttpUtil.sendOkHttpRequest("http://192.168.1.130:8080/12306/User_Register.jsp",requestBody , new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final boolean state = Utility.parseLoginResponseXML(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (state) {
                                Intent intent = new Intent();
                                intent.putExtra("user_name", user_name);
                                intent.putExtra("password", password);
                                setResult(RESULT_OK, intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }
    }
    /**
     * 判断手机格式是否正确
     * @param mobiles
     * @return
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}" ;
        if (TextUtils.isEmpty(mobiles)) return false ;
        else return mobiles.matches( telRegex ) ;
    }
    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}

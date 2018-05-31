package com.example.tzj12306.UI;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tzj12306.R;
import com.example.tzj12306.db.IdCard;
import com.example.tzj12306.db.User;

import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfoActivity";
    private TextView userId;
    private TextView email;
    private TextView phoneNum;
    private Button back;
    private ListView IdCard_view;
    private IDcardAdapt adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userId = (TextView) findViewById(R.id.info_user_id);
        email = (TextView) findViewById(R.id.info_email);
        phoneNum = (TextView) findViewById(R.id.info_phone_num);
        back = (Button) findViewById(R.id.info_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IdCard_view = (ListView) findViewById(R.id.IDcard_list_view);

        User user =(User)getIntent().getParcelableExtra("user");
        userId.setText(user.getUserName());
        email.setText(user.getEmail());
        phoneNum.setText(user.getPhoneNum());
        adapter = new IDcardAdapt(UserInfoActivity.this,R.layout.idcard_item,user.getIdCards());
        IdCard_view.setAdapter(adapter);

    }
    public class IDcardAdapt extends ArrayAdapter<IdCard>{
        private int resourceId;
        public IDcardAdapt(Context context, int resource, List<IdCard> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IdCard idCard = getItem(position);
            View view;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            }else{
                view = convertView;
            }
            TextView id_tv = (TextView) view.findViewById(R.id.info_card_id);
            TextView name_tv = (TextView) view.findViewById(R.id.info_card_name);
            id_tv.setText(idCard.getCardId());
            name_tv.setText(idCard.getName());
            return view;
        }
    }

}

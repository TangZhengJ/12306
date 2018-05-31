package com.example.tzj12306.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tzj12306.R;
import com.example.tzj12306.UI.LoginActivity;
import com.example.tzj12306.UI.UserInfoActivity;
import com.example.tzj12306.db.User;

/**
 * Created by ${cqc} on 2017/8/24.
 */

public class PersonalFragment extends Fragment {
    private static final String TAG = "PersonalFragment";
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_message;
    private RelativeLayout nav_header;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        View headView = navigationView.inflateHeaderView(R.layout.nav_header);
        nav_header = (RelativeLayout) headView.findViewById(R.id.nav_header);
        user_name = (TextView) headView.findViewById(R.id.user_name);
        user_message = (TextView) headView.findViewById(R.id.user_message);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        user = new User();
        if (getActivity().getIntent().hasExtra("user")) {
            user = (User) getActivity().getIntent().getParcelableExtra("user");
        }
        if (user.isLoginFlag()) {
            user_name.setText(user.getUserName());
            user_message.setText("点击切换账号");
        } else {
            user_name.setText("登录/注册");
            user_message.setText("快速登录，同步信息");
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_info: {
                        if (user.isLoginFlag()) {
                            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
        });
        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isLoginFlag()) {
                    user.setLoginFlag(false);
                    Toast.makeText(getActivity(), "注销成功！", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

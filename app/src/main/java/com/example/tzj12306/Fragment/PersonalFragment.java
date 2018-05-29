package com.example.tzj12306.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tzj12306.R;
import com.example.tzj12306.UI.LoginActivity;
import com.example.tzj12306.db.User;

/**
 * Created by ${cqc} on 2017/8/24.
 */

public class PersonalFragment extends Fragment {
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_message;
    private RelativeLayout nav_header;
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
        super.onActivityCreated(savedInstanceState);
        User user = new User();
        if(getActivity().getIntent().hasExtra("user")){
            user = (User)getActivity().getIntent().getParcelableExtra("user");
        }
        if(user.isLoginFlag()){
            user_name.setText(user.getUserName());
            user_message.setText("点击切换账号");
        }
       nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

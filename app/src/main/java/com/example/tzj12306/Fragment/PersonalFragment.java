package com.example.tzj12306.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tzj12306.R;
import com.example.tzj12306.UI.LoginActivity;

/**
 * Created by ${cqc} on 2017/8/24.
 */

public class PersonalFragment extends Fragment {
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_id;
    private RelativeLayout nav_header;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_id = (TextView) view.findViewById(R.id.user_id);
        View headView = navigationView.inflateHeaderView(R.layout.nav_header);
        nav_header = (RelativeLayout) headView.findViewById(R.id.nav_header);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

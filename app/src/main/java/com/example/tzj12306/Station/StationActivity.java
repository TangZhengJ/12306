package com.example.tzj12306.Station;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.example.tzj12306.Database.Station;
import com.example.tzj12306.impl.OnItemClickListener;
import com.example.tzj12306.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {
    private final static String TAG = StationActivity.class.getSimpleName();
    SearchView searchView;
    RecyclerView _station;
    List<Station> _Info;
    List<Station> find_Info;
    ListAdapter find_adapter;
    ListAdapter _adapter;
    OnItemClickListener myonItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        _station = (RecyclerView)findViewById(R.id.rv_station);
        _station.setLayoutManager(new LinearLayoutManager(this));

        _Info =  DataSupport.findAll(Station.class);
        _adapter = new ListAdapter(_Info,myonItemClickListener);
        _station.setAdapter( _adapter);

        myonItemClickListener = new OnItemClickListener() {
            private TextView tv_station;
            @Override
            public void onItemClick(View view, int position) {
                tv_station = (TextView) view.findViewById(R.id.tv_station_name);
                Intent intent = new Intent();
                intent.putExtra("data_return",tv_station.getText());
                setResult(RESULT_OK,intent);
                finish();
            }
        };

        find_Info = new ArrayList<Station>();
        searchView = (SearchView)findViewById(R.id.sv_station);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    Toast.makeText(StationActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    _station.setAdapter(_adapter);
                }
                else {
                    find_Info.clear();
                    for (int i = 0; i < _Info.size(); i++) {
                        Station station = _Info.get(i);
                        if (station.getAbbreviate().equals(query)) {
                            find_Info.add(station);
                        } else if (station.getCity_name().equals(query)) {
                            find_Info.add(station);
                        } else if (station.getStation_name().equals(query)) {
                            find_Info.add(station);
                        }
                    }
                    if (find_Info.size() == 0) {
                        Toast.makeText(StationActivity.this, "查找的车站不在列表中！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StationActivity.this, "查询成功！", Toast.LENGTH_SHORT).show();
                        find_adapter = new StationActivity.ListAdapter(find_Info, myonItemClickListener);
                        _station.setAdapter(find_adapter);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    _station.setAdapter(_adapter);
                }
                else
                {
                    find_Info.clear();
                    for(int i = 0; i < _Info.size(); i++)
                    {
                        Station station = _Info.get(i);
                        if(station.getAbbreviate().contains(newText)){
                            find_Info.add(station);
                        }
                        else if(station.getCity_name().contains(newText)){
                            find_Info.add(station);
                        }
                        else if(station.getStation_name().contains(newText)){
                            find_Info.add(station);
                        }
                    }
                    find_adapter = new ListAdapter(find_Info, myonItemClickListener);
                    find_adapter.notifyDataSetChanged();
                    _station.setAdapter(find_adapter);
                }
                return true;
            }
        });
    }


    private class ListHolder extends RecyclerView.ViewHolder{
        TextView tv_id;// id
        TextView tv_station_name;// 姓名
        TextView tv_abbreviate;// 拼音
        TextView tv_city_name;// 备注
        public ListHolder(View view) {
            super(view);
            tv_id = view.findViewById(R.id.tv_id);
            tv_station_name = view.findViewById(R.id.tv_station_name);
            tv_abbreviate = view.findViewById(R.id.tv_abbreviate);
            tv_city_name = view.findViewById(R.id.tv_city_name);
        }
    }
    private class ListAdapter extends RecyclerView.Adapter<ListHolder>{
        private List<Station> _Info;
        private OnItemClickListener _myonItemClickListener;
        public ListAdapter(List<Station> Info, OnItemClickListener myonItemClickListener){

            _Info = Info;
            _myonItemClickListener = myonItemClickListener;
        }
        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_station,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myonItemClickListener.onItemClick(v, (int) v.getTag());
                }
            });
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {

            Station info = _Info.get(position);
            holder.itemView.setTag(position);
            holder.tv_id .setText(String.format("%d",info.getId()));
            holder.tv_station_name.setText(info.getStation_name());
            holder.tv_abbreviate.setText(info.getAbbreviate());
            holder.tv_city_name.setText(info.getCity_name());
        }
        @Override
        public int getItemCount() {
            return _Info.size();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
    }
}


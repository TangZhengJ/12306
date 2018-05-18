package com.example.tzj12306.UI;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.db.TrianInfo;
import com.example.tzj12306.impl.ActionBarClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryActivity extends MyBaseActivity implements ActionBarClickListener
{
    private final static String TAG = QueryActivity.class.getSimpleName();
    String start;
    String end;
    String date;
    int year;
    int month;
    int day;
    Calendar calendar_r = Calendar.getInstance();
    final int year_r = calendar_r.get(Calendar.YEAR);
    final int month_r = calendar_r.get(Calendar.MONTH)+1;
    final int day_r = calendar_r.get(Calendar.DAY_OF_MONTH);
    Calendar calendar;
    String date_r = (year_r+"-"+month_r+"-"+day_r);;
    RecyclerView recyclerView_train;
    List<TrianInfo> trianInfos;
    ListViewAdapter adapter;
    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    Date dt1 = null;
    Date dt2 = null;
    Button bt_prevday;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_query;
    }
    @Override
    protected void init() {
        final Button bt_selectday = (Button)findViewById(R.id.bt_selectday);
        bt_prevday = (Button)findViewById(R.id.bt_prevday);
        final Button bt_nextday = (Button)findViewById(R.id.bt_nextday);
        Intent intent = getIntent();
       calendar = Calendar.getInstance();

        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        year = intent.getIntExtra("year",year_r);
        month = intent.getIntExtra("month",month_r);
        day = intent.getIntExtra("day",day_r);
        calendar.set(year,month-1,day);

        date = (year+"-"+month+"-"+day);
        bt_selectday.setText(date);
        setMyActionBar(R.id.actionbar_query,start+"->"+end, R.mipmap.ic_left_light, "取消", R.mipmap.ic_right_light, "确认", this);


        bt_prevday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH)+1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                date = (year+"-"+month+"-"+day);
                bt_selectday.setText(date);
                try {
                    dt1 = sdf.parse(date);
                    dt2 = sdf.parse(date_r);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dt1.getTime() <= dt2.getTime()){
                    bt_prevday.setClickable(false);
                    bt_prevday.setBackgroundResource(R.color.primaryDark);
                }
            }
        });
        bt_nextday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH,1);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH)+1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                date = (year+"-"+month+"-"+day);
                bt_selectday.setText(date);
                try {
                    dt1 = sdf.parse(date);
                    dt2 = sdf.parse(date_r);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dt1.getTime() > dt2.getTime()) {
                    bt_prevday.setClickable(true);
                    bt_prevday.setBackgroundResource(R.color.primary);
                }
            }
        });
        bt_selectday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(QueryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearofDate, int monthOfYear, int dayOfMonth) {
                        year = yearofDate;
                        month = monthOfYear+1;
                        day = dayOfMonth;
                        calendar.set(year,month-1,day);
                        bt_selectday .setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    }
                }, year, month-1, day);
                datePickerDialog.show();
            }
        });

        recyclerView_train = (RecyclerView) findViewById(R.id.rv_train);
        recyclerView_train.setAdapter(adapter);
        recyclerView_train.setLayoutManager(new LinearLayoutManager(this));
        trianInfos = new ArrayList<TrianInfo>();
        for (int i=0;i<10;i++) {
            trianInfos.add(new TrianInfo("北京", "上海"));
            trianInfos.add(new TrianInfo("杭州", "嘉兴"));
        }
        adapter = new ListViewAdapter(trianInfos);
        recyclerView_train.setAdapter(adapter);
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
        Toast.makeText(this, "确定", Toast.LENGTH_SHORT).show();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_start;
        TextView tv_end;
        TextView tv_num;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_start = (TextView) itemView.findViewById(R.id.tv_start_station);
            tv_end = (TextView) itemView.findViewById(R.id.tv_end_station);
            tv_num = (TextView) itemView.findViewById(R.id.tv_train_num);
        }
    }
    private class ListViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
        private List<TrianInfo> _trianInfos;
        public ListViewAdapter(List<TrianInfo> trianInfos) {
            _trianInfos = trianInfos;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_train,parent,false);
            return new RecyclerViewHolder(view);
        }


        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            TrianInfo info = trianInfos.get(position);
            holder.tv_start.setText(info.getStart());
            holder.tv_end.setText(info.getEnd());
            holder.tv_num.setText(String.format("%d",position+1));
        }

        @Override
        public int getItemCount() {
            return trianInfos.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = (year+"-"+month+"-"+day);
        try {
            dt1 = sdf.parse(date);
            dt2 = sdf.parse(date_r);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() <= dt2.getTime()){
            bt_prevday.setClickable(false);
            bt_prevday.setBackgroundResource(R.color.primaryDark);
        }else {
            bt_prevday.setClickable(true);
            bt_prevday.setBackgroundResource(R.color.primary);
        }
    }
}



package com.example.tzj12306.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tzj12306.Database.GetStationList;
import com.example.tzj12306.Database.Station;
import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.Station.ChooseAreaActivity;
import com.example.tzj12306.Station.HistoryInfo;
import com.example.tzj12306.Station.StationActivity;
import com.example.tzj12306.impl.OnItemClickListener;
import com.example.tzj12306.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectActivity extends MyBaseActivity {
    // 返回的结果码
    private final static int REQUESTCODE_START = 1;
    private final static int REQUESTCODE_END= 2;
    private final static String TAG = SelectActivity.class.getSimpleName();
//    private ImageView bingPicImg ;
    Button bt_start;
    Button bt_end;
    Button bt_clean_history;
    ImageButton ib_exchange;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    RecyclerView rv_history_station;
    List<HistoryInfo> historys;
    RecyclerViewAdapt history_adapter;
    OnItemClickListener myitemlistener;
    String start;
    String end;
    @Override
    protected int getContentViewId() {
        return R.layout.active_select;
    }

    @Override
    protected void init() {
        setContentView(R.layout.active_select);
        Button title = (Button)findViewById(R.id.tv_actionbar_title);
        title.setText("车票预订");
        final Button button_date = (Button)findViewById(R.id.button_date);
        final Button button_time = (Button)findViewById(R.id.button_time);
        bt_start = (Button)findViewById(R.id.bt_start);
        bt_end = (Button)findViewById(R.id.bt_end);
        ib_exchange = (ImageButton) findViewById(R.id.ib_exchange);
        Button button_query = (Button)findViewById(R.id.button_query);
//        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        final CheckedTextView checkbox_student = (CheckedTextView)findViewById(R.id.checkbox_student);
        bt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceStation(REQUESTCODE_END);
            }
        });
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoiceStation(REQUESTCODE_START);
            }
        });
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String bingPic = prefs.getString("bing_pic",null);
//        if(bingPic != null){
//            Glide.with(this).load(bingPic).into(bingPicImg);
//        }else{
//            loadBingPic();
//        }
        ib_exchange.setOnClickListener(new View.OnClickListener() {
            private String str;
            @Override
            public void onClick(View v) {
                str = bt_start.getText().toString();
                bt_start.setText(bt_end.getText());
                bt_end.setText(str);
            }
        });

        button_date.setText(year+"-"+month+"-"+day);

        button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteFile();
                start = bt_start.getText().toString();
                end = bt_end.getText().toString();
                String date = button_date.getText().toString();
                Intent intent = new Intent(SelectActivity.this, QueryActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                startActivity(intent);
            }
        });
        checkbox_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkbox_student.setChecked(!checkbox_student.isChecked());
            }
        });
        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearofDate, int monthOfYear, int dayOfMonth) {
                        year = yearofDate;
                        month = monthOfYear+1;
                        day = dayOfMonth;
                        button_date .setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    }
                }, year, month-1, day);
                datePickerDialog.show();
            }
        });
        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoisDialog();
            }

            private void showChoisDialog() {
                Builder builder=new Builder(SelectActivity.this);
                builder.setTitle("请选择时间:");
                final String []items=new String[]{"00:00-24:00","00:00-6:00","06:00-12:00","12:00-18:00","18:00-24:00"};
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    //which指的是用户选择的条目的下标
                    //dialog:触发这个方法的对话框
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        button_time.setText(items[which]);
                        dialog.dismiss();//当用户选择了一个值后，对话框消失
                    }
                });
                builder.show();
            }
        });
        bt_clean_history = (Button)findViewById(R.id.bt_hisory_clean);
        bt_clean_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream out = null;
                BufferedWriter writer = null;
                try {
                    out = openFileOutput("history", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    historys = new ArrayList<HistoryInfo>();
                    history_adapter = new RecyclerViewAdapt(historys,myitemlistener);
                    rv_history_station.setAdapter(history_adapter);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try{
                        if (writer != null){
                            writer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    private void ReadFile(List<HistoryInfo> historys) {
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = openFileInput("history");
            reader = new BufferedReader(new InputStreamReader(in));
            String str="";
            while( (str = reader.readLine())!=null)
            {
                historys.add(new HistoryInfo(str,reader.readLine()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteFile() {

        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("history", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(bt_start.getText().toString()+"\n");
            writer.write(bt_end.getText().toString()+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_START:
                if (resultCode == RESULT_OK) {
                    bt_start.setText(data.getStringExtra("data_return"));
                    Log.d("SelectActivity",data.getStringExtra("data_return"));
                }
                break;
            case REQUESTCODE_END:{
                if (resultCode == RESULT_OK) {

                    bt_end.setText(data.getStringExtra("data_return"));
                    Log.d("SelectActivity",data.getStringExtra("data_return"));
                }
                break;
            }
            default:
        }
    }
    @Override
    protected void onStart() {
        Log.d(TAG, "SelectActivity onStart");
        rv_history_station = (RecyclerView) findViewById(R.id.rv_history_station);
        LinearLayoutManager linearLayoutManager =   new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        linearLayoutManager.setReverseLayout(true);//列表翻转
        rv_history_station.setLayoutManager(linearLayoutManager);
        historys = new ArrayList<HistoryInfo>();
        ReadFile(historys);

        history_adapter = new RecyclerViewAdapt(historys,myitemlistener);

        rv_history_station.setAdapter(history_adapter);
        myitemlistener= new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView history = (TextView) view.findViewById(R.id.tv_hisory_station);
                HistoryInfo historyInfo = historys.get(position);
                bt_start.setText(historyInfo.getHistory_start());
                bt_end.setText(historyInfo.getHistory_end());
            }
        };
        super.onStart();
    }

    private class RecyclerViewAdapt extends RecyclerView.Adapter<RVHoldelder> {
        private List<HistoryInfo> historys;
        private OnItemClickListener onItemClickListener;
        public RecyclerViewAdapt(List<HistoryInfo> historys, OnItemClickListener myitemlistener) {
            this.historys = historys;
            this.onItemClickListener = myitemlistener;
        }

        @Override
        public RVHoldelder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_station,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myitemlistener.onItemClick(v, (Integer) v.getTag());
                }
            });
            return new RVHoldelder(view);
        }

        @Override
        public void onBindViewHolder(RVHoldelder holder, int position) {
            HistoryInfo info = historys.get(position);
            holder.tv_history.setText(info.getHistory_start()+"--"+info.getHistory_end());
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return historys.size();
        }

    }
    private class RVHoldelder extends RecyclerView.ViewHolder {
        private TextView tv_history;
        public RVHoldelder(View itemView) {
            super(itemView);
            tv_history = (TextView) itemView.findViewById(R.id.tv_hisory_station);
        }
    }
    private void ChoiceStation(int REQUESTCODE) {
        Intent intent = new Intent(SelectActivity.this, ChooseAreaActivity.class);
        startActivityForResult(intent,REQUESTCODE);
    }
    /**
     * 加载背景图
     */
//    private void loadBingPic() {
//        String requestBinPic = "http://guolin.tech/api/bing_pic";
//        HttpUtil.sendOkHttpRequest(requestBinPic, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String bingPic = response.body().string();
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SelectActivity.this).edit();
//                editor.putString("bing_pic",bingPic);
//                editor.apply();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(SelectActivity.this).load(bingPic).into(bingPicImg);
//                    }
//                });
//            }
//        });
//    }
}

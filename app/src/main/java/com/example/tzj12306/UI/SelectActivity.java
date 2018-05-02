package com.example.tzj12306.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

import com.example.tzj12306.Database.GetStationList;
import com.example.tzj12306.Database.Station;
import com.example.tzj12306.MyActionBar.MyBaseActivity;
import com.example.tzj12306.R;
import com.example.tzj12306.Station.HistoryInfo;
import com.example.tzj12306.Station.StationActivity;
import com.example.tzj12306.impl.OnItemClickListener;

import org.litepal.LitePal;
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

public class SelectActivity extends MyBaseActivity {
    // 返回的结果码
    private final static int REQUESTCODE_START = 1;
    private final static int REQUESTCODE_END= 2;
    private final static String TAG = SelectActivity.class.getSimpleName();
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

    @Override
    protected void onResume() {
        Log.d(TAG, "SelectActivity onResume");

        super.onResume();
    }

    @Override
    protected void onPause() {

        Log.d(TAG, "SelectActivity onPause");
        super.onPause();
    }
    @Override
    protected void onStop() {

        Log.d(TAG, "SelectActivity onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "SelectActivity onDestroy");
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {

        Log.d(TAG, "SelectActivity onBackPressed");
        super.onBackPressed();
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
//    public void executeAssetsSQL(SQLiteDatabase db, String dbfilepath) {
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new InputStreamReader(getAssets().open(dbfilepath)));
//            String line;
//            String buffer = "";
//            //开启事务
//            db.beginTransaction();
//            while ((line = in.readLine()) != null) {
//                buffer += line;
//                if (line.trim().endsWith(";")) {
//                    db.execSQL(buffer.replace(";", ""));
//                    buffer = "";
//                }
//            }
//            //设置事务标志为成功，当结束事务时就会提交事务
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("db-error", e.toString());
//        } finally {
//            //事务结束
//            db.endTransaction();
//            try {
//                if (in != null)
//                    in.close();
//            } catch (Exception e) {
//                Log.e("db-error", e.toString());
//            }
//        }
//    }
    private void ChoiceStation(int REQUESTCODE) {
        if(DataSupport.count(Station.class)==0){
            GetStationList getStationList = new GetStationList();
            getStationList.sendRequestWithOkhttp();
//            SQLiteDatabase db = LitePal.getDatabase();
//            executeAssetsSQL(db , "station.sql");

            Log.d(TAG, String.valueOf(DataSupport.count(Station.class)));
        }
        Intent intent = new Intent(SelectActivity.this, StationActivity.class);
        startActivityForResult(intent,REQUESTCODE);
    }

}

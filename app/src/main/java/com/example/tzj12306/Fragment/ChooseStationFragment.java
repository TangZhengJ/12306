package com.example.tzj12306.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.tzj12306.R;
import com.example.tzj12306.UI.ChooseAreaActivity;
import com.example.tzj12306.UI.QueryActivity;
import com.example.tzj12306.UI.SelectActivity;
import com.example.tzj12306.db.HistoryInfo;
import com.example.tzj12306.impl.OnItemClickListener;
import com.example.tzj12306.impl.WeatherIdListener;
import com.wakehao.bar.BottomNavigationBar;

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

/**
 * Created by ${cqc} on 2017/8/24.
 */

public class ChooseStationFragment extends Fragment {
    private final static int REQUESTCODE_START = 1;
    private final static int REQUESTCODE_END= 2;
    private final static String TAG = "ChooseStationFragment";
    private Button bt_start;
    private Button bt_end;
    private Button bt_clean_history;
    private Button title;
    private ImageButton ib_exchange;
    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH)+1;
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private RecyclerView rv_history_station;
    private List<HistoryInfo> historys;
    private RecyclerViewAdapt history_adapter;
    private OnItemClickListener myitemlistener;
    private Button button_date;
    private Button button_time;
    private Button button_query;
    private CheckedTextView checkbox_student;
    private String start;
    private String end;
    private static WeatherIdListener mWeatherIdListener;
    private String weather_start = "CN101210107";
    private String weather_end = "CN101210101";
    BottomNavigationBar bottomNavigationBar;
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_choose_station, container, false);
        title = (Button)getActivity().findViewById(R.id.tv_actionbar_title);
        button_date = (Button)view.findViewById(R.id.button_date);
        button_time = (Button)view.findViewById(R.id.button_time);
        bt_start = (Button)view.findViewById(R.id.bt_start);
        bt_end = (Button)view.findViewById(R.id.bt_end);
        ib_exchange = (ImageButton) view.findViewById(R.id.ib_exchange);
        button_query = (Button)view.findViewById(R.id.button_query);
        checkbox_student = (CheckedTextView)view.findViewById(R.id.checkbox_student);
        bt_clean_history = (Button)view.findViewById(R.id.bt_hisory_clean);
        rv_history_station = (RecyclerView) view.findViewById(R.id.rv_history_station);
        bottomNavigationBar = (BottomNavigationBar)getActivity().findViewById(R.id.bottomBars);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
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
                Intent intent = new Intent(getActivity(), QueryActivity.class);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
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
        bt_clean_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream out = null;
                BufferedWriter writer = null;
                try {
                    out = getActivity().openFileOutput("history", Context.MODE_PRIVATE);
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
    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        LinearLayoutManager linearLayoutManager =   new LinearLayoutManager(getActivity());
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
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_START:
                if (resultCode == getActivity().RESULT_OK) {
                    String city_start = data.getStringExtra("data_return");
                    bt_start.setText(city_start);
                    weather_start=data.getStringExtra("weather_id");
                    Log.d("weather_start",weather_start);
                    mWeatherIdListener.onCityStart(weather_start,city_start);
                }
                break;
            case REQUESTCODE_END:{
                if (resultCode == getActivity().RESULT_OK) {
                    String city_end = data.getStringExtra("data_return");
                    bt_end.setText(city_end);
                    weather_end=data.getStringExtra("weather_id");
                    Log.d("weather_end",weather_end);
                    mWeatherIdListener.onCityEnd(weather_end,city_end);
                }
                break;
            }
            default:
        }
    }
    private void ReadFile(List<HistoryInfo> historys) {
        FileInputStream in = null;
        BufferedReader reader = null;
        try {
            in = getActivity().openFileInput("history");
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
            out = getActivity().openFileOutput("history", Context.MODE_APPEND);
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
    private void ChoiceStation(int REQUESTCODE) {
        Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
        startActivityForResult(intent,REQUESTCODE);
    }
    private class RVHoldelder extends RecyclerView.ViewHolder {
        private TextView tv_history;
        public RVHoldelder(View itemView) {
            super(itemView);
            tv_history = (TextView) itemView.findViewById(R.id.tv_hisory_station);
        }
    }
    public class RecyclerViewAdapt extends RecyclerView.Adapter<RVHoldelder> {
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
    public static void setOnWeatherIdListener(WeatherIdListener weatherIdListener){
        mWeatherIdListener = weatherIdListener;
    }

}

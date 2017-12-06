package com.example.nick.myfirstapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.habitOperation;
import com.example.nick.util.baseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HabitCheckActivity extends AppCompatActivity {

    private  static final String TAG = "HabitCheckActivity";

    //自定义变量
    private EditText subjectEdit;
    private EditText titleEdit;
    private EditText dateEdit;
    private EditText minuteEdit;
    private EditText accuracyEdit;
    private EditText contentEdit;

    //底部四个布局按钮
    private LinearLayout layoutMinute;
    private LinearLayout layoutCalculator;
    private LinearLayout layoutCancel;
    private LinearLayout layoutSave;

    private int id;

    private Date start;
    private Date end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_habit_check);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        Log.i(TAG,"bundle id=" + id );

        //获取对象
        subjectEdit = (EditText) findViewById(R.id.subject4hc);
        titleEdit = (EditText) findViewById(R.id.showtitle4hc);
        dateEdit = (EditText) findViewById(R.id.showdate4hc);
        minuteEdit = (EditText) findViewById(R.id.minutes);
        accuracyEdit = (EditText) findViewById(R.id.accuracy);
        contentEdit = (EditText) findViewById(R.id.comment);
        layoutMinute = (LinearLayout) findViewById(R.id.layout_minute);
        layoutCalculator = (LinearLayout) findViewById(R.id.layout_calculate);
        layoutCancel = (LinearLayout) findViewById(R.id.layout_cancel4hc);
        layoutSave = (LinearLayout) findViewById(R.id.layout_save4hc);

        controlItem();
        setData(id);

        //点击"计时"按钮布局 设置分钟
        layoutMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置用时
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Log.i(TAG,"start:" + start.getTime() + " end:" + end.getTime());
                //Log.i(TAG,"fmt start:" + sdf.format(start) + " end:" +sdf.format(end));
                long interval = (end.getTime() - start.getTime()) / (1000 * 60 );
                minuteEdit.setText((new Long(interval)).toString());
            }
        });

        layoutMinute.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutMinute.setBackgroundColor(Color.CYAN);
                    layoutCalculator.setBackgroundColor(Color.TRANSPARENT);
                    layoutCancel.setBackgroundColor(Color.TRANSPARENT);
                    layoutSave.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        //点击"时间"按钮布局 设置时间
        layoutCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitCheckActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.cal_dialog, null);
                final EditText quantum = (EditText) view.findViewById(R.id.quantum);
                final EditText accurate = (EditText) view.findViewById(R.id.accurate);

                builder.setView(view);
                builder.setTitle("请录入以下信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 省略check

                        int q = Integer.parseInt(quantum.getText().toString());
                        int a = Integer.parseInt(accurate.getText().toString());
                        int accuracy =  a * 100 / q;
                        accuracyEdit.setText((new Integer(accuracy)).toString());

                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();

            }
        });
        layoutCalculator.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutMinute.setBackgroundColor(Color.TRANSPARENT);
                    layoutCalculator.setBackgroundColor(Color.CYAN);
                    layoutCancel.setBackgroundColor(Color.TRANSPARENT);
                    layoutSave.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        //点击"取消"按钮
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minuteEdit.setText("");
                minuteEdit.setHint("45");
                accuracyEdit.setText("");
                accuracyEdit.setHint("100");
            }
        });
        layoutCancel.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutMinute.setBackgroundColor(Color.TRANSPARENT);
                    layoutCalculator.setBackgroundColor(Color.TRANSPARENT);
                    layoutCancel.setBackgroundColor(Color.CYAN);
                    layoutSave.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        //点击"保存"按钮
        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //确认保存按钮
                new AlertDialog.Builder(HabitCheckActivity.this).setTitle("确认保存吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String,Object> habit = new HashMap<String, Object>();
                                habit.put("s",subjectEdit.getText().toString());
                                habit.put("t",titleEdit.getText().toString());
                                habit.put("m",minuteEdit.getText().toString());
                                habit.put("rt",accuracyEdit.getText().toString());
                                habit.put("c",contentEdit.getText().toString());
                                habit.put("f",2);
                                updateData(habit);

                                finish();

                            }

                        })
                        .setNegativeButton("返  回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        layoutSave.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutMinute.setBackgroundColor(Color.TRANSPARENT);
                    layoutCalculator.setBackgroundColor(Color.TRANSPARENT);
                    layoutCancel.setBackgroundColor(Color.TRANSPARENT);
                    layoutSave.setBackgroundColor(Color.CYAN);
                }
                return false;
            }
        });
    }

    private void controlItem() {
        subjectEdit.setEnabled(false);
        titleEdit.setEnabled(false);
        dateEdit.setEnabled(false);
    }

    private void setData(int id) {
        Date dt = new Date();
        if (id > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(HabitCheckActivity.this, constant.DB_NAME);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ContentValues result = habitOperation.query(db,id);

            subjectEdit.setText((String)result.get("subject"));
            titleEdit.setText((String)result.get("title"));
            contentEdit.setText((String)result.get("comment"));
            dt.setTime((long)result.get("start"));
            end = new Date();
            end.setTime((long)result.get("end"));   //TODO check
            minuteEdit.setText(String.valueOf(result.get("minutes")));
            accuracyEdit.setText(String.valueOf(result.get("correctrate")));
            Log.i(TAG,"start:" + result.get("start") + " end:" + result.get("end"));
        }else { //TODO err
            subjectEdit.setText("");
            titleEdit.setText("");
            contentEdit.setText("");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        dateEdit.setText(sdf.format(dt));
        start = dt;
    }

    private void updateData(HashMap<String,Object> habit) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(HabitCheckActivity.this, constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            habitOperation.update(db,id,habit);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

package com.example.nick.myfirstapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.dicOperation;
import com.example.nick.db.habitOperation;
import com.example.nick.util.baseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HabitEditActivity extends AppCompatActivity {

    private  static final String TAG = "HabitEditActivity";

    //自定义变量
    private EditText subjectEdit;
    private EditText titleEdit;
    private EditText dateEdit;
    private EditText timeEdit;
    private EditText contentEdit;

    //底部四个布局按钮
    private LinearLayout layoutDate;
    private LinearLayout layoutTime;
    private LinearLayout layoutCancel;
    private LinearLayout layoutSave;

    //定义显示时间控件
    private Calendar calendar; //通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private int id;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_habit_edit);

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        mode = bundle.getInt("mode");
        Log.i(TAG,"bundle id=" + id + " mode=" + mode);

        //获取对象
        subjectEdit = (EditText) findViewById(R.id.subject);
        titleEdit = (EditText) findViewById(R.id.showtitle);
        dateEdit = (EditText) findViewById(R.id.showdate);
        timeEdit = (EditText) findViewById(R.id.showtime);
        contentEdit = (EditText) findViewById(R.id.comment);
        layoutDate = (LinearLayout) findViewById(R.id.layout_date);
        layoutTime = (LinearLayout) findViewById(R.id.layout_time);
        layoutCancel = (LinearLayout) findViewById(R.id.layout_cancel);
        layoutSave = (LinearLayout) findViewById(R.id.layout_save);

        controlItem();
        setData();

        calendar = Calendar.getInstance();

        //点击"日期"按钮布局 设置日期
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitEditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                //设置日期简略显示 否则详细显示 包括:星期\周
                datePicker.setCalendarViewShown(false);
                //初始化当前日期
                calendar.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), null);
                //设置date布局
                builder.setView(view);
                builder.setTitle("设置日期信息");

                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //日期格式
                        StringBuffer sb = new StringBuffer();
                        //赋值后面闹钟使用
                        mYear = datePicker.getYear();
                        mMonth = datePicker.getMonth();
                        mDay = datePicker.getDayOfMonth();
                        sb.append(baseUtil.formatDate(mYear,mMonth,mDay));
                        dateEdit.setText(sb);
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

        layoutDate.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutDate.setBackgroundColor(Color.CYAN);
                    layoutTime.setBackgroundColor(Color.TRANSPARENT);
                    layoutCancel.setBackgroundColor(Color.TRANSPARENT);
                    layoutSave.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

        //点击"时间"按钮布局 设置时间
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(HabitEditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                //初始化时间
                calendar.setTimeInMillis(System.currentTimeMillis());
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("设置时间信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHour = timePicker.getCurrentHour();
                        mMinute = timePicker.getCurrentMinute();
                        //时间小于10的数字 前面补0 如01:12:00
                        timeEdit.setText(baseUtil.formatTime(mHour,mMinute));
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
        layoutTime.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutDate.setBackgroundColor(Color.TRANSPARENT);
                    layoutTime.setBackgroundColor(Color.CYAN);
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
                dateEdit.setText("");
                dateEdit.setHint("2017-10-01");
                timeEdit.setText("");
                timeEdit.setHint("18:00:00");
                contentEdit.setText("");
                contentEdit.setHint("记录旅途中的备注信息...");
            }
        });
        layoutCancel.setOnTouchListener(new View.OnTouchListener() { //设置布局背景
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)  {
                    layoutDate.setBackgroundColor(Color.TRANSPARENT);
                    layoutTime.setBackgroundColor(Color.TRANSPARENT);
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
                new AlertDialog.Builder(HabitEditActivity.this).setTitle("确认保存吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

                                    Date dt = sdf.parse(dateEdit.getText() + " " + timeEdit.getText());
                                    Log.i(TAG,"Parse :" + dt.toString());

                                    HashMap<String,Object> habit = new HashMap<String, Object>();
                                    habit.put("s",subjectEdit.getText().toString());
                                    habit.put("t",titleEdit.getText().toString());
                                    habit.put("sd",dt);
                                    habit.put("c",contentEdit.getText().toString());
                                    switch (mode) {
                                        case 1:
                                            updateData(habit);
                                            break;
                                        case 2:
                                            updEndate(dt);
                                            break;
                                        default:
                                            insertData(habit);  // 数据库插入操作
                                    }
                                    /*
                                    if (mode == 0) {
                                        insertData(habit);  // 数据库插入操作
                                    }else if(mode == 1) {
                                        updateData(habit);
                                    } else {
                                        updEndate(dt);
                                    }*/
                                }catch (ParseException e) {
                                    Log.i(TAG,"Error:" + e.getMessage());
                                }

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
                    layoutDate.setBackgroundColor(Color.TRANSPARENT);
                    layoutTime.setBackgroundColor(Color.TRANSPARENT);
                    layoutCancel.setBackgroundColor(Color.TRANSPARENT);
                    layoutSave.setBackgroundColor(Color.CYAN);
                }
                return false;
            }
        });

    }

    private void controlItem() {
        switch (mode) {
            case 1:
                subjectEdit.setEnabled(false);
                titleEdit.setEnabled(false);
                layoutDate.setEnabled(false);
                layoutTime.setEnabled(true);
                layoutCancel.setEnabled(false);
                break;
            case 2:
                subjectEdit.setEnabled(false);
                titleEdit.setEnabled(false);
                layoutDate.setEnabled(false);
                layoutTime.setEnabled(true);
                layoutCancel.setEnabled(false);
                break;
            default:
                subjectEdit.setEnabled(true);
                titleEdit.setEnabled(true);
                layoutDate.setEnabled(true);
                layoutTime.setEnabled(true);
                layoutCancel.setEnabled(true);
        }
    }

    private void setData() {
        Date dt = new Date();
        Date now = new Date();
        if (id > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(HabitEditActivity.this, constant.DB_NAME);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ContentValues result = habitOperation.query(db,id);

            subjectEdit.setText((String)result.get("subject"));
            titleEdit.setText((String)result.get("title"));
            contentEdit.setText((String)result.get("comment"));
            dt.setTime((long)result.get("start"));
        }else {
            subjectEdit.setText("");
            titleEdit.setText("");
            contentEdit.setText("");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        dateEdit.setText(sdf.format(dt));
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        switch (mode) {
            case 1:
                timeEdit.setText(sdf1.format(dt));
                break;
            default:
                timeEdit.setText(sdf1.format(now));
        }
    }

    private void insertData(HashMap<String,Object> habit) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(HabitEditActivity.this, constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            habitOperation.insert(db,habit);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateData(HashMap<String,Object> habit) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(HabitEditActivity.this, constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            habitOperation.update(db,id,habit);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updEndate(Date date) {
        DatabaseHelper dbHelper = new DatabaseHelper(HabitEditActivity.this, constant.DB_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        habitOperation.update(db,id,date);
        db.close();
    }

}

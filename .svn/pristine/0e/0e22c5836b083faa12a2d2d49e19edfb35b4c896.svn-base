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
import com.example.nick.db.improveOperation;
import com.example.nick.util.baseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ImproveEditActivity extends AppCompatActivity {

    private  static final String TAG = "ImproveEditActivity";

    //自定义变量
    private EditText subjectEdit;
    private EditText titleEdit;
    private EditText dateEdit;
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
        setContentView(R.layout.activity_improve_edit);

        getInfoFromBundle();
        getItemsFromActivity();
        controlItem(mode);
        setData(id);

        calendar = Calendar.getInstance();

        //点击"日期"按钮布局 设置日期
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(ImproveEditActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ImproveEditActivity.this);
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
                        //timeEdit.setText(baseUtil.formatTime(mHour,mMinute));
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
                contentEdit.setText("");
                contentEdit.setHint("记录改进事项的具体措施...");
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
                new AlertDialog.Builder(ImproveEditActivity.this).setTitle("确认保存吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

                                    Date dt = sdf.parse(dateEdit.getText() + " 00:00:00");
                                    Log.i(TAG,"Parse :" + dt.toString());

                                    HashMap<String,Object> improve = new HashMap<String, Object>();
                                    improve.put("s",subjectEdit.getText().toString());
                                    improve.put("t",titleEdit.getText().toString());
                                    improve.put("r",dt);
                                    improve.put("c",contentEdit.getText().toString());
                                    if (mode == 0) {
                                        insertData(improve);  // 数据库插入操作人
                                    }else {
                                        updateData(improve);
                                    }
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

    private void getInfoFromBundle() {
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        mode = bundle.getInt("mode");
        Log.i(TAG,"bundle id=" + id + " mode=" + mode);
    }

    private void getItemsFromActivity() {
        //获取对象
        subjectEdit = (EditText) findViewById(R.id.subject4i);
        titleEdit = (EditText) findViewById(R.id.showtitle4i);
        dateEdit = (EditText) findViewById(R.id.showdate4i);
        contentEdit = (EditText) findViewById(R.id.comment4i);
        layoutDate = (LinearLayout) findViewById(R.id.layout_date4i);
        layoutTime = (LinearLayout) findViewById(R.id.layout_time4i);
        layoutCancel = (LinearLayout) findViewById(R.id.layout_cancel4i);
        layoutSave = (LinearLayout) findViewById(R.id.layout_save4i);
    }

    private void controlItem(int mode) {
        switch (mode) {
            case 1:
                subjectEdit.setEnabled(false);
                titleEdit.setEnabled(false);
                layoutDate.setEnabled(false);
                layoutTime.setEnabled(false);
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

    private void setData(int id) {
        Date dt = new Date();
        if (id > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(ImproveEditActivity.this, constant.DB_NAME);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ContentValues result = improveOperation.query(db,id);

            subjectEdit.setText((String)result.get("subject"));
            titleEdit.setText((String)result.get("title"));
            contentEdit.setText((String)result.get("comment"));
            dt.setTime((long)result.get("register"));
        }else {
            subjectEdit.setText("");
            titleEdit.setText("");
            contentEdit.setText("");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        dateEdit.setText(sdf.format(dt));
    }

    private void insertData(HashMap<String,Object> improve) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(ImproveEditActivity.this, constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            improveOperation.insert(db,improve);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateData(HashMap<String,Object> improve) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(ImproveEditActivity.this, constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            improveOperation.update(db,id,improve);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

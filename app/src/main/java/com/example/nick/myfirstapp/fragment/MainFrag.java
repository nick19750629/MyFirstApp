package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.dicOperation;
import com.example.nick.domain.Practice;
import com.example.nick.myfirstapp.MainActivity;
import com.example.nick.myfirstapp.R;
import com.example.nick.util.baseUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class MainFrag extends Fragment{
    private static final String TAG = "MainFrag";

    //component
    private View rootView;
    private TextView tip;
    private TextView question;
    private FloatingActionButton fab;

    //data
    private List<HashMap<String,String>> dic;
    private Practice practice;
    private HashMap<String,String> word;

    //flag
    private int subject;
    private int status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG ,"onCreateView begin");
        rootView = inflater.inflate(R.layout.content_main,null);
        initView();
        initValue();
        setTextValue(question,tip,word);
        //setFloatingActionButton();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG ,"onActivityCreated begin");
        super.onActivityCreated(savedInstanceState);
        fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        setFloatingActionButton();
    }

    private void initView() {
        tip = (TextView)rootView.findViewById(R.id.txtTip);
        question = (TextView)rootView.findViewById(R.id.txtWord);
    }

    private void initValue() {
        dic = new ArrayList<HashMap<String,String>>();
        readData();
        practice = new Practice(dic,30);
        word = practice.getFirstWord();
    }

    private void setFloatingActionButton() {
        fab.setVisibility(rootView.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = getAnswer(word);
                Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                status = 0;
                if (!practice.isLast()){
                    snack.setAction("回答正确！", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            status = 1;
                        }
                    });
                }else{
                    snack.setAction("重新开始？", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            status = 9;
                            //Toast.makeText(MainActivity.this, "暂时保留", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                snack.setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        switch (status) {
                            case 0:
                                countData("mistake",word);
                                Toast.makeText(rootView.getContext(),"没有记住",Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                countData("correct",word);
                                if (!practice.isLast()) {
                                    word = practice.getNextWord();
                                    setTextValue(question, tip, word);
                                }
                                Toast.makeText(rootView.getContext(), "回答正确", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                word = practice.getFirstWord();
                                setTextValue(question, tip, word);
                                Toast.makeText(rootView.getContext(), "重新练习", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                snack.show();
            }
        });

    }

    private void readData() {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(),constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //SQLiteDatabase db = dbUtil.getDatabase("dic");
            dicOperation.createTable(db);
            boolean imported = false;
            if (dicOperation.getCountByMode(db,0) > 0) {
                getDataFromDb(db);
            }else {
                getDataFromFile(db);
            }
            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void countData(String key,HashMap<String,String> word) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //SQLiteDatabase db = dbUtil.getDatabase("dic");
            dicOperation.count(db,key,word);
            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void getDataFromFile(SQLiteDatabase db) {
        try{
            //得到资源中的Raw数据流
            InputStream in = getResources().openRawResource(R.raw.test);

            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);
                dic.clear();

                db.beginTransaction();

                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    HashMap word = new HashMap<String,String>();
                    String b[] = line.split("\t");
                    if (b.length == 4) {
                        word.put("e",b[0]); //English
                        word.put("p",b[1]); //phonetic
                        word.put("c",b[2]); //Chinese
                        word.put("o",b[3]); //origin
                        dicOperation.insertBatch(db,word);
                        dic.add(word);
                    } else {
                        //TODO error
                    }
                }

                db.setTransactionSuccessful();
                db.endTransaction();
            }

            //关闭
            in.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromDb(SQLiteDatabase db) {
        try{
            dic = dicOperation.queryAll(db);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getAnswer(HashMap<String,String> word) {
        String msg = "";
        if (word == null) return msg;
        switch (subject %2) {
            case 0:
                msg = word.get("c");
                break;
            default:
                msg = word.get("e") + "\t" + word.get("p");
        }
        return msg;
    }

    private void setTextValue(TextView text,TextView tip,HashMap<String,String> word) {

        if (word == null) {
            tip.setText("本轮测试结束");
            return;
        }

        subject = baseUtil.dice();

        if ((subject % 2) == 0) {
            text.setText(word.get("e"));
            tip.setText("请给出中文解释！");
        }else {
            text.setText(word.get("c"));
            tip.setText("请拼出英文单词，并正确发音！");
        }
    }
}

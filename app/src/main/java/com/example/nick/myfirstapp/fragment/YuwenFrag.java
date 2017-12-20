package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.cwordOperation;
import com.example.nick.db.dbUtil;
import com.example.nick.db.dicOperation;
import com.example.nick.db.practiceDetailOperation;
import com.example.nick.db.practiceOperation;
import com.example.nick.db.subjectOperation;
import com.example.nick.domain.Practice;
import com.example.nick.myfirstapp.R;
import com.example.nick.util.baseUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class YuwenFrag extends Fragment{
    private static final String TAG = "YuwenFrag";

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

    private int pid;
    private int param;
    private String course;
    private String selected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG ,"onCreateView begin");
        rootView = inflater.inflate(R.layout.content_yuwen,null);
        Bundle bundle = getArguments();
        param = bundle.getInt("param");
        course = bundle.getString("course");
        selected = bundle.getString("select");
        Log.i(TAG,"selected=" + selected);
        initView();
        initValue();
        createPractice();
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
        tip = (TextView)rootView.findViewById(R.id.txtTip4c);
        question = (TextView)rootView.findViewById(R.id.txtWord4c);
    }

    private void initValue() {
        int max_cnt;
        dic = new ArrayList<HashMap<String,String>>();
        readData();
        if (course.equals(constant.SUB_CHINESE)) {
            max_cnt = Integer.parseInt(getSettingInfo(rootView,"chinese_amount")) ;
        }else {
            max_cnt = Integer.parseInt(getSettingInfo(rootView,"english_amount")) ;
        }
        practice = new Practice(dic,max_cnt);
        word = practice.getFirstWord();
    }

    private void setFloatingActionButton() {
        fab.setVisibility(rootView.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4 aton v
                if (!practice.isLast()) {
                    word = practice.getNextWord();
                    setTextValue(question, tip, word);
                }else {
                    //gotoProcticeConfirm(view);
                    updMinutes();
                    initFrag(view);
                    Toast.makeText(rootView.getContext(),"练习完成！",Toast.LENGTH_LONG).show();
                }
               /* 4 大众v
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
               */
            }
        });

    }

    private void readData() {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(),constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //SQLiteDatabase db = dbUtil.getDatabase("dic");
            //TODO 表的检查重置在初期做？
            dicOperation.createTable(db);
            cwordOperation.createTable(db);

            //boolean imported = false;
            if ("".equals(selected)){
                if (course.equals(constant.SUB_CHINESE)) {
                    if (cwordOperation.getCountByMode(db,0) > 0) {
                        getDataFromDb(db);
                    }else {
                        getDataFromFile(db);
                    }
                } else {
                    if (dicOperation.getCountByMode(db,0) > 0) {
                        getDataFromDb(db);
                    }else {
                        getDataFromFile(db);
                    }
                }
            }else {
                String[] idx = selected.split(",");
                Log.i(TAG,"idx.length=" + idx.length);
                dic = new ArrayList<HashMap<String,String>>();
                for (int i = 0;i < idx.length;i++) {
                    Log.i(TAG,"before getSelectData i=" + i);
                    dic.addAll(getSelectData(db,getModule(db,idx[i])));
                }
            }
            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createPractice() {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            HashMap<String,Object> practice = new HashMap<String, Object>();
            practice.put("c",course);
            if (course.equals(constant.SUB_CHINESE)) {
                practice.put("t", constant.copt_name[param]);
            } else {
                practice.put("t", constant.eopt_name[param]);
            }
            practice.put("s","全部");
            practice.put("d",new Date());
            pid = ((Long)practiceOperation.insert(db,practice)).intValue();
            Log.i(TAG,"pid=" + pid);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updMinutes() {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ContentValues info = practiceOperation.query(db,pid);
            Date st = dbUtil.transDateType((long)info.get("start")) ;
            Date ed = new Date();
            long diff = ed.getTime() - st.getTime();
            int minutes = (int) (diff / (1000 * 60)) ;
            practiceOperation.updMinutes(db,pid,minutes);
            Log.i(TAG,"pid=" + pid + " minutes=" + minutes);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createDetail(String question,String answer) {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            HashMap<String,Object> practice = new HashMap<String, Object>();
            practice.put("p",pid);
            practice.put("q",question);
            practice.put("a",answer);

            practiceDetailOperation.insert(db,practice);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromFile(SQLiteDatabase db) {
        try{
            //得到资源中的Raw数据流
            int rawId;
            if (course.equals(constant.SUB_CHINESE)) {
                rawId = R.raw.cword;
            } else {
                rawId = R.raw.test;
            }
            InputStream in = getResources().openRawResource(rawId);


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
                        if (course.equals(constant.SUB_CHINESE)) {
                            cwordOperation.insertBatch(db, word);
                        } else {
                            dicOperation.insertBatch(db,word);
                        }
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
            if (course.equals(constant.SUB_CHINESE)) {
                dic = cwordOperation.queryAll(db);
            } else {
                dic = dicOperation.queryAll(db);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private List<HashMap<String,String>> getSelectData(SQLiteDatabase db,String pModule) {
        List<HashMap<String,String>> ret = new ArrayList<HashMap<String,String>>();
        try{
            if (course.equals(constant.SUB_CHINESE)) {
                ret = cwordOperation.queryByModule(db,pModule);
            } else {
                ret = dicOperation.queryByModule(db,pModule);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            return ret;
        }
    }

    private void setTextValue(TextView text,TextView tip,HashMap<String,String> word) {
        String question,answer;

        if (word == null) {
            tip.setText("本轮测试结束");
            return;
        }

        if (param == 3 || param == 4) {
            subject = baseUtil.dice();
        } else {
            subject = param;
        }

        if (course.equals(constant.SUB_CHINESE)) {
            switch (subject %3) {
                case 0:
                    text.setText(word.get("e"));
                    tip.setText("给字词注音！");
                    question = "【看汉字写拼音】" + word.get("e");
                    answer = word.get("p");
                    break;
                case 1:
                    String q = parsePinyin(word);
                    text.setText(q);
                    tip.setText("根据拼音，请写出正确的字或词！");
                    question = "【看拼音写汉字】" + q;
                    answer = word.get("e");
                    break;
                default:
                    text.setText(word.get("c"));
                    tip.setText("根据意思，请写出正确的字或词！");
                    question = "【望义生文】" + word.get("c");
                    answer = word.get("e");
                    break;
            }
        } else {
            if ((subject % 2) == 0) {
                text.setText(word.get("e"));
                tip.setText("请给出中文解释！");
                question = "【英译汉】" + word.get("e");
                answer = word.get("c");
            }else {
                text.setText(word.get("c"));
                tip.setText("请拼出英文单词，并正确发音！");
                question = "【汉译英】" + word.get("c");
                answer = word.get("e") + "　"+ word.get("p");
            }
        }
        createDetail(question,answer);
    }

    private void initFrag(View view) {
        Fragment fragment;
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragment = new PracticeConfirmFrag();
        Bundle bundle = new Bundle();
        bundle.putInt("id", pid);
        Log.i(TAG,"param out pid=" + pid);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    /*4 大众v
    private String getAnswer(HashMap<String,String> word) {
        String msg = "";
        switch (subject %3) {
            case 0:
                msg = word.get("c");
                break;
            case 1:
                msg = word.get("e");
                break;
            default:
                msg = word.get("e") + "\t" + word.get("p");
        }
        return msg;
    }

    private void countData(String key,HashMap<String,String> word) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cwordOperation.count(db,key,word);
            db.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

    private String parsePinyin(HashMap<String,String> word) {
        String ret = "";

        if (word.get("e").length() == 1) {  //单字拼音特别处理
            //Log.i(TAG,"单字拼音特别处理");
            String a[] = word.get("c").split("：");
            List<String> words = new ArrayList<String>();
            for (int i = 0; i < a.length; i++) {
                if (a[i].contains("～")) {
                    String b[] = a[i].split("。");
                    for (int j = 0; j < b.length; j++) {
                        if (b[j].contains("～")) {
                            words.add(b[j].replace("～",word.get("p")));
                            //Log.i(TAG,"候补" + b[j]);
                        }
                    }
                }
            }
            if (words.size() > 0) {
                int idx = (int)(Math.random() * words.size());
                ret = words.get(idx);
            }else {
                ret = word.get("p");
            }
        }else {
            ret = word.get("p");
        }
        return ret;
    }

    private String getModule(SQLiteDatabase db,String idx) {
        List<HashMap<String,String>> modules = subjectOperation.queryBySubject(db,course);
        Log.i(TAG,"course=" + course + " and size=" + modules.size());
        if (modules.size() > Integer.parseInt(idx)) {
            return modules.get(Integer.parseInt(idx)).get("d");
        }else {
            return "";
        }
    }
    public static String getSettingInfo(View v,String key) {
        SharedPreferences setting  = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        return setting.getString(key,"30");
    }

}

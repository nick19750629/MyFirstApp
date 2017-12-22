package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;  

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.cwordOperation;
import com.example.nick.db.dicOperation;
import com.example.nick.db.habitOperation;
import com.example.nick.db.practiceDetailOperation;
import com.example.nick.db.practiceOperation;
import com.example.nick.db.progressOperation;
import com.example.nick.db.subjectOperation;
import com.example.nick.myfirstapp.MainActivity;
import com.example.nick.myfirstapp.R;
import com.example.nick.util.httpUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class ImportFrag extends Fragment {

    private static final String TAG = "ImportFrag";
    private View rootView;
     private MyHandler myhandler = new MyHandler(this);  

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_import,null);

        Button btn = (Button)rootView.findViewById(R.id.btnImport);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"数据导入开始！",Toast.LENGTH_LONG).show()；
                initDb(v);
                impDbFromFile(v);
                impDbFromFile4c(v);
                impDbFromFile4s(v);
                impDbFromFile4p(v);
                Toast.makeText(v.getContext(),"数据导入完成！",Toast.LENGTH_LONG).show();
            }
        });
        Button snd = (Button) rootView.findViewById(R.id.btnSend);
        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"发送邮件开始！");
                //sendMail();sendMail2();
                sendMail3();
                Log.i(TAG,"发送邮件完成！");
            }
        });

        Button web = (Button) rootView.findViewById(R.id.btnRemoteCall);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "远程调用开始！");
                remoteCall();
                Log.i(TAG, "远程调用完成！");
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(rootView.INVISIBLE);
    }

    private void impDbFromFile(View view) {
        try{
            Log.i(TAG,"Import Begin");
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            dicOperation.createTable(db);
            if (dicOperation.getCountByMode(db,constant.CNT_ALL) > 0) {
                dicOperation.deleteAll(db);
            }

            //得到资源中的Raw数据流
            InputStream in = getResources().openRawResource(R.raw.test);
            int cnt = 0;
            db.beginTransaction();

            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);

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
                        cnt ++;
                        Log.i(TAG,"IMPORT " + cnt + ":" + word.get("e"));
                        //Toast.makeText(view.getContext(),"导入第" + cnt + "条",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG,"import error." + line);
                        //TODO error
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            //关闭
            in.close();
            Log.i(TAG,"Import Over");

        }catch(Exception e){
            Log.i(TAG,"Import Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void impDbFromFile4c(View view) {
        try{
            Log.i(TAG,"Import Begin");
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cwordOperation.createTable(db);
            if (cwordOperation.getCountByMode(db,constant.CNT_ALL) > 0) {
                cwordOperation.deleteAll(db);
            }

            //得到资源中的Raw数据流
            InputStream in = getResources().openRawResource(R.raw.cword);
            int cnt = 0;
            db.beginTransaction();

            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    HashMap word = new HashMap<String,String>();
                    String b[] = line.split("\t");
                    if (b.length == 4) {
                        word.put("e",b[0]); //zici
                        word.put("p",b[1]); //pinyin
                        word.put("c",b[2]); //shiyi
                        word.put("o",b[3]); //origin
                        cwordOperation.insertBatch(db,word);
                        cnt ++;
                        Log.i(TAG,"IMPORT " + cnt + ":" + word.get("e"));
                        //Toast.makeText(view.getContext(),"导入第" + cnt + "条",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG,"import error." + line);
                        //TODO error
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            //关闭
            in.close();
            Log.i(TAG,"Import Over");

        }catch(Exception e){
            Log.i(TAG,"Import Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void impDbFromFile4s(View view) {
        try{
            Log.i(TAG,"Import Begin");
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            subjectOperation.createTable(db);
            if (subjectOperation.getCount(db) > 0) {
                subjectOperation.deleteAll(db);
            }

            //得到资源中的Raw数据流
            InputStream in = getResources().openRawResource(R.raw.subject);
            int cnt = 0;
            db.beginTransaction();

            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    HashMap word = new HashMap<String,String>();
                    String b[] = line.split("\t");
                    if (b.length == 3) {
                        word.put("s",b[0]); //subject
                        word.put("d",b[1]); //display_sort
                        word.put("m",b[2]); //module
                        subjectOperation.insertBatch(db,word);
                        cnt ++;
                        Log.i(TAG,"IMPORT " + cnt + ":" + word.get("s") + word.get("d"));
                        //Toast.makeText(view.getContext(),"导入第" + cnt + "条",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG,"import error." + line);
                        //TODO error
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            //关闭
            in.close();
            Log.i(TAG,"Import Over");

        }catch(Exception e){
            Log.i(TAG,"Import Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void impDbFromFile4p(View view) {
        try{
            Log.i(TAG,"Import Begin");
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            progressOperation.createTable(db);
            if (progressOperation.getCount(db) > 0) {
                progressOperation.deleteAll(db);
            }

            //得到资源中的Raw数据流
            InputStream in = getResources().openRawResource(R.raw.progress);
            int cnt = 0;
            db.beginTransaction();

            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    HashMap word = new HashMap<String,String>();
                    String b[] = line.split("\t");
                    if (b.length == 3) {
                        word.put("s",b[0]); //subject
                        word.put("p",b[1]); //display_sort
                        word.put("a",b[2]); //module
                        progressOperation.insertBatch(db,word);
                        cnt ++;
                        Log.i(TAG,"IMPORT " + cnt + ":" + word.get("s"));
                        //Toast.makeText(view.getContext(),"导入第" + cnt + "条",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG,"import error." + line);
                        //TODO error
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

            //关闭
            in.close();
            Log.i(TAG,"Import Over");

        }catch(Exception e){
            Log.i(TAG,"Import Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initDb(View view) {
        try{
            Log.i(TAG,"initDb Begin");
            DatabaseHelper dbHelper = new DatabaseHelper(view.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            practiceOperation.createTable(db);
            practiceDetailOperation.createTable(db);
            db.close();
        }catch(Exception e){
            Log.i(TAG,"initDb Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMail() {
        try{
            //Toast.makeText(rootView.getContext(),"send mail begin",Toast.LENGTH_LONG).show();

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] receiver = new String[] {"nick@dhc.com.cn"};
            String subject = "学习习惯数据导出";
            String content = "学习习惯数据导出";

            //Toast.makeText(rootView.getContext(),"send mail step 001",Toast.LENGTH_LONG).show();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("text/csv");
            // 设置邮件发收人
            email.putExtra(Intent.EXTRA_EMAIL, receiver);
            // 设置邮件标题
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            // 设置邮件内容
            email.putExtra(Intent.EXTRA_TEXT, content);

            //Toast.makeText(rootView.getContext(),"send mail step 002",Toast.LENGTH_LONG).show();

            // 设置邮件附件
            File sdCardDir =  Environment.getExternalStorageDirectory();
            Log.i(TAG,"SD:" + sdCardDir.getPath());
            String fileName = habitOperation.queryDataToFile(db,sdCardDir.getPath());
            File file = new File(sdCardDir.getPath() + "/" + fileName);

            //Toast.makeText(rootView.getContext(),"query data to file " + fileName,Toast.LENGTH_LONG).show();
            Log.i(TAG,file.toString());
            //email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/" + fileName));
            //email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///storage/emulated/0/" + fileName));
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            /*
            //export cword
            fileName = cwordOperation.queryDataToFile(db,sdCardDir.getPath());
            file = new File(sdCardDir.getPath() + "/" + fileName);
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            //Toast.makeText(rootView.getContext(),"attachfile" ,Toast.LENGTH_LONG).show();
            */

            // 调用系统的邮件系统
            startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

            db.close();
        }catch(Exception e){
            Log.e(TAG,"Exception" + e.getMessage());
            //Toast.makeText(rootView.getContext(),"Exception" + e.getMessage() ,Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendMail2() {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] receiver = new String[] {"nech7506@hotmail.com"};  //TO变更
            String subject = "易错内容整理";
            String content = "根据导出数据进行易错内容整理";

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("text/csv");
            // 设置邮件发收人
            email.putExtra(Intent.EXTRA_EMAIL, receiver);
            // 设置邮件标题
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            // 设置邮件内容
            email.putExtra(Intent.EXTRA_TEXT, content);

            // 设置邮件附件
            File sdCardDir =  Environment.getExternalStorageDirectory();
            //Log.i(TAG,"SD:" + sdCardDir.getPath());
            String fileName = practiceDetailOperation.queryDataToFile(db,sdCardDir.getPath());
            File file = new File(sdCardDir.getPath() + "/" + fileName);

            Log.i(TAG,file.toString());
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            // 调用系统的邮件系统
            startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

            db.close();
        }catch(Exception e){
            Log.e(TAG,"Exception" + e.getMessage());
            //Toast.makeText(rootView.getContext(),"Exception" + e.getMessage() ,Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendMail3() {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] receiver = new String[] {"nech7506@hotmail.com"};  //TO变更
            String subject = "易错内容整理";
            StringBuffer content = new StringBuffer();
            content.append("根据导出数据进行易错内容整理\n");
            content.append("question\tanswer\n");

            List<HashMap<String,Object>> result = practiceDetailOperation.queryDataToRpt(db);
            for (int i = 0;i < result.size();i++) {
                content.append(result.get(i).get("q")).append("\t").append(result.get(i).get("a")).append("\n");
            }

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("text/csv");
            // 设置邮件发收人
            email.putExtra(Intent.EXTRA_EMAIL, receiver);
            // 设置邮件标题
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            // 设置邮件内容
            email.putExtra(Intent.EXTRA_TEXT, content.toString());

            // 调用系统的邮件系统
            startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

            db.close();
        }catch(Exception e){
            Log.e(TAG,"Exception" + e.getMessage());
            //Toast.makeText(rootView.getContext(),"Exception" + e.getMessage() ,Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void remoteCall() {
        //开启访问数据库线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = httpUtil.testByPost("u", "p");
                Bundle bundle = new Bundle();
                bundle.putString("result",str);
                Message msg = new Message();
                msg.setData(bundle);
                myhandler.sendMessage(msg);
            }
        }).start();
    }
    
    //弱引用，防止内存泄露  
    private static class MyHandler extends Handler {
        private final WeakReference<ImportFrag> mFrag;
  
        public MyHandler(ImportFrag frag) {
            mFrag = new WeakReference<ImportFrag>(frag);
        }  
  
        @Override  
        public void handleMessage(Message msg) {  
            System.out.println(msg);  
            if (mFrag.get() == null) {
                return;  
            }
            mFrag.get().updateUIThread(msg);
        }  
    }      
    
    //配合子线程更新UI线程  
    private void updateUIThread(Message msg){
        Bundle bundle = new Bundle();  
        bundle = msg.getData();  
        String result = bundle.getString("result");  
        Toast.makeText(rootView.getContext(), result, Toast.LENGTH_SHORT).show();
    }  
    
}

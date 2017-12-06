package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.habitOperation;
import com.example.nick.db.progressOperation;
import com.example.nick.db.subjectOperation;
import com.example.nick.myfirstapp.R;
import com.example.nick.myfirstapp.list.habitInfo;
import com.example.nick.myfirstapp.list.subjectInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class TestFrag extends Fragment {

    private static final String TAG = "TestFrag";
    private View rootView;
    private ListView listView;
    private FloatingActionButton fab;

    private List<subjectInfo> mlistInfo = new ArrayList<subjectInfo>();  //声明一个list，动态存储要显示的信息
    private List<HashMap<String,Object>> dbResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_test,null);

        readData();
        listView = (ListView) rootView.findViewById(R.id.lvDetail4m);
        setInfo();

        ListViewAdapter adapter = new TestFrag.ListViewAdapter(rootView.getContext() );

        listView.setAdapter(adapter);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(rootView.INVISIBLE);

        return rootView;
    }

    private void readData() {
        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            progressOperation.createTable(db);

            dbResult = progressOperation.queryAll(db);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getProgressName(String subject,int progress) {
        String ret = "";

        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            subjectOperation.createTable(db);

            List<HashMap<String,String>> lstRet = subjectOperation.queryByKey(db,subject,progress);

            if (lstRet.size() >= 1) {
                ret = lstRet.get(0).get("m");
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            return ret;
        }

    }

    private void updProgress(String subject,int progress) {
        String ret = "";

        try{
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            progressOperation.updProgress(db,subject,progress);

            db.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfo() {
        mlistInfo.clear();
        for (int i = 0;i < dbResult.size();i++){
            subjectInfo info = new subjectInfo();
            String subject = (String)dbResult.get(i).get("s");
            int p = (int)dbResult.get(i).get("p");
            int s = (int)dbResult.get(i).get("a");
            info.setTitle(getProgressName(subject,p));
            info.setProgress(p % 100);
            info.setMax(s);
            Log.i(TAG,"p=" + p % 100 + "s=" + s);
            info.setSubject(subject);
            if (subject.equals("语文")) {
                info.setAvatar(R.drawable.ic_icon_yuwen);
                info.setVersion("人教版");
            } else {
                info.setAvatar(R.drawable.ic_menu_english);
                info.setVersion("外研社");
            }
            mlistInfo.add(info);
        }
    }

    public class ListViewAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public ListViewAdapter(Context context) {
            // TODO Auto-generated constructor stub
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mlistInfo.size();
        }


        public Object getItem(int position) {
            return mlistInfo.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final RecordViewHold holder;

            Log.i(TAG,"ListViewAdapter getView" + position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_subject, null);
                holder = new RecordViewHold();
                holder.title = (TextView) convertView.findViewById(R.id.title4Proc);
                holder.subject = (TextView) convertView.findViewById(R.id.subject4Proc);
                holder.version  = (TextView) convertView.findViewById(R.id.version4Proc);
                holder.seek = (SeekBar) convertView.findViewById(R.id.seekBar);

                holder.img = (ImageView) convertView.findViewById(R.id.imgSub);

                convertView.setTag(holder);
            } else {
                holder = (RecordViewHold)convertView.getTag();
            }

            subjectInfo info = mlistInfo.get(position);

            holder.subject.setVisibility(convertView.INVISIBLE);
            holder.title.setText(info.getTitle());    //填入相应的值
            holder.subject.setText(info.getSubject());
            holder.version.setText(info.getVersion());
            holder.seek.setProgress(info.getProgress());
            holder.seek.setMax(info.getMax());
            holder.img.setImageResource(info.getAvatar());

            holder.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        Log.i(TAG,"subject:" + holder.subject.getText().toString() + " progress:" + progress);

                        String t = getProgressName(holder.subject.getText().toString(),progress + 1500); //TODO progress换算
                        holder.title.setText(t);
                        //进度更新
                        updProgress(holder.subject.getText().toString(),progress + 1500);
                    }
                }
            });

            return convertView;
        }

        class RecordViewHold {
            TextView title;
            SeekBar seek;
            ImageView img;
            TextView version;
            TextView subject;
        }
    }
}

package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.cwordOperation;
import com.example.nick.db.dicOperation;
import com.example.nick.myfirstapp.R;
import com.example.nick.myfirstapp.list.historyInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class HistoryFrag extends Fragment  {

    private static final String TAG = "HistoryFrag";

    //Control
    private View rootView;
    private FloatingActionButton fab;
    private ListView listView;

    private List<historyInfo> mlistInfo = new ArrayList<historyInfo>();  //声明一个list，动态存储要显示的信息
    private List<HashMap<String,String>> historys;

    @Override
    public void onResume() {
        super.onResume();
        //返回数据刷新
        //refreshList();
        Log.i(TAG,"onResume");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        setFloatingActionButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_history,null);
        readData();

        listView = (ListView) rootView.findViewById(R.id.lvDetail4h);
        setInfo();

        listView.setAdapter(new ListViewAdapter(mlistInfo));

        return rootView;
    }


    private void refreshList() {
        readData();
        setInfo();
        listView.setAdapter(new ListViewAdapter(mlistInfo));
    }

    private void setFloatingActionButton() {
        fab.setVisibility(rootView.INVISIBLE);
    }

    private void readData() {
        try{
            //得到资源中的Raw数据流
            int rawId = R.raw.history;
            InputStream in = getResources().openRawResource(rawId);
            int i = 0;

            historys = new ArrayList<HashMap<String,String>>();
            if (in != null) {
                InputStreamReader inputreader = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    if (i == 0) {
                        i = 1 ;
                        continue;
                    }
                    HashMap word = new HashMap<String,String>();
                    String b[] = line.split("\t");
                    if (b.length == 3) {
                        word.put("d",b[0]); //date
                        word.put("v",b[1]); //version
                        word.put("c",b[2]); //comment
                        historys.add(word);
                    } else {
                        //TODO error
                    }
                }

            }

            //关闭
            in.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setInfo(){
        mlistInfo.clear();
        for (int i = 0;i < historys.size(); i++) {
            historyInfo information = new historyInfo();
            information.setId(i);
            StringBuffer dtl = new StringBuffer();
            dtl.append(historys.get(i).get("d")).append("\t").append(historys.get(i).get("v"));
            information.setTitle(dtl.toString());
            information.setDetails(historys.get(i).get("c"));
            mlistInfo.add(information); //将新的info对象加入到信息列表中
        }
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<historyInfo> mlistInfo) {
            // TODO Auto-generated constructor stub
            itemViews = new View[mlistInfo.size()];
            for(int i=0;i<mlistInfo.size();i++){
                historyInfo getInfo=(historyInfo)mlistInfo.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(
                        getInfo.getTitle(), getInfo.getDetails()
                );
            }
        }

        public int getCount() {
            return itemViews.length;
        }

        public View getItem(int position) {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }

        //绘制Item的函数
        private View makeItemView(String strTitle, String strText) {
            LayoutInflater inflater = (LayoutInflater) rootView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.item_history, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.title4h);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.info4h);
            text.setText(strText);

            return itemView;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }
}

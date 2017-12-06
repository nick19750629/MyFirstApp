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
import com.example.nick.db.practiceOperation;
import com.example.nick.myfirstapp.R;
import com.example.nick.myfirstapp.list.practiceInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class PracticeResultFrag extends Fragment  {

    private static final String TAG = "PracticeResultFrag";

    /*
    @Override
    public void onTimeInputComplete(String datetime) {
        Log.i(TAG,"onTimeInputComplete:" + datetime);
    }*/

    //Control
    private View rootView;
    private FloatingActionButton fab;
    private ListView listView;

    private List<practiceInfo> mlistInfo = new ArrayList<practiceInfo>();  //声明一个list，动态存储要显示的信息
    private List<HashMap<String,Object>> dbResult;

    @Override
    public void onResume() {
        super.onResume();
        //返回数据刷新
        refreshList();
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
        rootView = inflater.inflate(R.layout.content_habit,null);
        readData();

        listView = (ListView) rootView.findViewById(R.id.lvDetail);
        setInfo();

        listView.setAdapter(new ListViewAdapter(mlistInfo));

        //长按菜单显示
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("操作");
                conMenu.add(0, 0, 0, "强制删除");
            }
        });

        return rootView;
    }

    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)aItem.getMenuInfo();
        int idx = (int)info.id;
        int MID = (int)dbResult.get(idx).get("i");

        switch (aItem.getItemId()) {
            case 0:
                delete(MID);
                refreshList();
                return true;
        }
        return false;
    }

    private void refreshList() {
        readData();
        setInfo();
        listView.setAdapter(new ListViewAdapter(mlistInfo));
    }

    private void setFloatingActionButton() {
        fab.setVisibility(rootView.INVISIBLE);
    }

    private void delete(int id) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            practiceOperation.deleteByKey(db,id);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readData() {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            dbResult = practiceOperation.queryAll(db);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setInfo(){
        mlistInfo.clear();
        int max = (dbResult.size() > constant.MAX_DISP ? constant.MAX_DISP : dbResult.size());
        for (int i = 0;i < max; i++) {
            practiceInfo information = new practiceInfo();
            information.setId((int)dbResult.get(i).get("i"));
            information.setTitle((String)dbResult.get(i).get("t"));
            StringBuffer dtl = new StringBuffer();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
            dtl.append(sdf.format((Date)dbResult.get(i).get("d")).toString()).append(",\t");
            if ((int)dbResult.get(i).get("m") > 0) {
                dtl.append("用时").append(dbResult.get(i).get("m")).append("分,\t");
            }
            if ((int)dbResult.get(i).get("r") > 0) {
                dtl.append("得分").append(dbResult.get(i).get("r")).append("．\t");
            }
            dtl.append("注：").append((String)dbResult.get(i).get("s"));
            information.setDetails(dtl.toString());
            String subject = (String)dbResult.get(i).get("c");
            if (constant.SUB_ENGLISH.equals(subject)) {
                information.setAvatar(R.drawable.ic_menu_english);
            } else if (constant.SUB_MATH.equals(subject)){
                information.setAvatar(R.drawable.ic_icon_math);
            } else {
                information.setAvatar(R.drawable.ic_icon_yuwen);
            }
            mlistInfo.add(information); //将新的info对象加入到信息列表中
        }
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<practiceInfo> mlistInfo) {
            // TODO Auto-generated constructor stub
            itemViews = new View[mlistInfo.size()];
            for(int i=0;i<mlistInfo.size();i++){
                practiceInfo getInfo=(practiceInfo)mlistInfo.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(
                        getInfo.getTitle(), getInfo.getDetails(),getInfo.getAvatar()
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
        private View makeItemView(String strTitle, String strText, int resId) {
            LayoutInflater inflater = (LayoutInflater) rootView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.item_habit, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.info);
            text.setText(strText);
            ImageView image = (ImageView) itemView.findViewById(R.id.img);
            image.setImageResource(resId);

            return itemView;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }
}

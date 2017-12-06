package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.habitOperation;
import com.example.nick.db.practiceDetailOperation;
import com.example.nick.db.practiceOperation;
import com.example.nick.myfirstapp.HabitCheckActivity;
import com.example.nick.myfirstapp.HabitEditActivity;
import com.example.nick.myfirstapp.R;
import com.example.nick.myfirstapp.list.habitInfo;
import com.example.nick.myfirstapp.list.practiceInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class PracticeConfirmFrag extends Fragment  {

    private static final String TAG = "PracticeConfirmFrag";

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

    private int pid;

    private ListViewAdapter mAdapter;

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
        rootView = inflater.inflate(R.layout.content_practice_confirm,null);
        Bundle bundle = getArguments();
        pid = bundle.getInt("id");
        Log.i(TAG,"param in pid=" + pid);
        listView = (ListView) rootView.findViewById(R.id.lvDetail4pc);

        refreshList();

        readData();
        setInfo();
        mAdapter = new ListViewAdapter(mlistInfo);
        listView.setAdapter(mAdapter);

        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                practiceInfo getObject = mlistInfo.get(position);   //通过position获取所点击的对象
                int infoId = getObject.getId(); //获取信息id
                String infoTitle = getObject.getTitle();    //获取信息标题
                String infoDetails = getObject.getDetails();    //获取信息详情

                if (getObject.getAvatar() != R.drawable.ic_icon_mistake) {
                    getObject.setAvatar(R.drawable.ic_icon_mistake);
                } else {
                    getObject.setAvatar(0);
                }

                getObject.setTitle(infoTitle);

                setStatus(view,infoId);
                mlistInfo.set(position,getObject);
                Log.i(TAG,"click " + position );
                updateItem(position);

                mAdapter.notifyDataSetChanged();
                //Toast显示测试
                //Toast.makeText(rootView.getContext(), "信息ID:"+infoId,Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    /**
     * 第三种方法 调用一次getView()方法；Google推荐的做法
     *
     * @param position 要更新的位置
     */
    private void updateItem(int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            Log.i(TAG,"updateItem" + position );
            mAdapter.getView(position, view, listView);
        }
    }

    private void refreshList() {
        readData();
        setInfo();
        listView.setAdapter(mAdapter);
    }

    private void setFloatingActionButton() {
        fab.setVisibility(rootView.VISIBLE);
        fab.setImageResource(R.drawable.ic_icon_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mistakes = 0;
                for (int i = 0;i < mlistInfo.size(); i++) {
                    practiceInfo info = mlistInfo.get(i);
                    if (info.getAvatar() == R.drawable.ic_icon_mistake) {
                        updDetailMiss(info.getId());
                        mistakes ++ ;
                        Log.i(TAG,"Upd Error id=" + info.getId());
                    }
                }
                int score = (mlistInfo.size() - mistakes) * 100 / mlistInfo.size();
                updScore(score);
                Log.i(TAG,"Upd score=" + score);
                //返回练习结果画面
                initFrag(view);
            }
        });
    }

    private void setStatus(View view,int id) {
        Log.i(TAG,"打叉 id=" + id);
    }

    private void readData() {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            dbResult = practiceDetailOperation.queryByPid(db,pid);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updDetailMiss(int id) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            practiceDetailOperation.update(db,id,1);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initFrag(View view) {
        Fragment fragment;
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragment = new PracticeResultFrag();
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    private void updScore(int score) {
        try{

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            practiceOperation.updScore(db, pid, score);

            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setInfo(){
        mlistInfo.clear();
        for (int i = 0;i < dbResult.size(); i++) {
            practiceInfo information = new practiceInfo();
            information.setId((int)dbResult.get(i).get("i"));
            information.setTitle((String)dbResult.get(i).get("a"));
            information.setDetails((String)dbResult.get(i).get("q"));
            int subject = ((Integer) dbResult.get(i).get("c")).intValue();
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
        private View makeItemView(String strQuestion, String strAnswer, int resId) {
            LayoutInflater inflater = (LayoutInflater) rootView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.item_practice, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView question = (TextView) itemView.findViewById(R.id.question);
            question.setText(strQuestion);    //填入相应的值
            TextView answer = (TextView) itemView.findViewById(R.id.answer);
            answer.setText(strAnswer);
            ImageView image = (ImageView) itemView.findViewById(R.id.img);
            image.setImageResource(resId);

            return itemView;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(TAG,"getView " + position );
            practiceInfo getInfo=(practiceInfo)mlistInfo.get(position);    //获取第i个对象
            //调用makeItemView，实例化一个Item
            itemViews[position]=makeItemView(
                    getInfo.getTitle(), getInfo.getDetails(),getInfo.getAvatar());
            return itemViews[position];
        }
    }
}

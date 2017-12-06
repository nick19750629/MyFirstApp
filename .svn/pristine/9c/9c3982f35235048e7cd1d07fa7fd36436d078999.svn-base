package com.example.nick.myfirstapp.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nick.constant.constant;

import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.dicOperation;
import com.example.nick.myfirstapp.R;

/**
 * Created by nick on 2017/10/09.
 */

public class SumFrag extends Fragment {

    private static final String TAG = "SumFrag";
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_sum,null);
        readData();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(rootView.INVISIBLE);
    }

    private void readData() {
        try {

            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            long lngSum = dicOperation.getCountByMode(db, constant.CNT_ALL);
            long lngUsed = dicOperation.getCountByMode(db, constant.CNT_USED);
            long lngCorrect = dicOperation.getSumByMode(db, constant.SUM_CORRECT);
            long lngMistake = dicOperation.getSumByMode(db, constant.SUM_MISTAKE);
            long lngTimes = lngCorrect + lngMistake;
            TextView sum = (TextView) rootView.findViewById(R.id.txtSum);
            sum.setText("本字典共收录了" + lngSum + "个单词！");
            TextView used = (TextView) rootView.findViewById(R.id.txtRecite);
            used.setText("你已经学习了" + lngUsed + "个单词！");
            TextView times = (TextView) rootView.findViewById(R.id.txtTimes);
            times.setText("你已经进行了" + lngTimes + "次学习！");
            TextView correct = (TextView) rootView.findViewById(R.id.txtCorrect);
            correct.setText("其中正确了" + lngCorrect + "次！");
            TextView mistake = (TextView) rootView.findViewById(R.id.txtMistake);
            mistake.setText("其中错误了" + lngMistake + "次！");

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

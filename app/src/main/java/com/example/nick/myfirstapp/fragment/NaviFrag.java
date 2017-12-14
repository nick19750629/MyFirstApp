package com.example.nick.myfirstapp.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.nick.constant.constant;
import com.example.nick.myfirstapp.dialog.selector;
import com.example.nick.myfirstapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nick on 2017/10/09.
 */

public class NaviFrag extends Fragment {

    private static final String TAG = "NaviFrag";
    private View rootView;

    private int param;
    private String course;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_navi,null);

        Bundle bundle = getArguments();
        course = bundle.getString("course");

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        int length;
        if (course.equals(constant.SUB_CHINESE)) {
            length = constant.CImageRes.length;
        } else {
            length = constant.EImageRes.length;
        }

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (course.equals(constant.SUB_CHINESE)) {
                map.put("ItemImage", constant.CImageRes[i]);//添加图像资源的ID
                map.put("ItemText", constant.copt_name[i]);//按序号做ItemText
            }
            if (course.equals(constant.SUB_ENGLISH)) {
                map.put("ItemImage", constant.EImageRes[i]);//添加图像资源的ID
                map.put("ItemText", constant.eopt_name[i]);//按序号做ItemText
            }
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(rootView.getContext(),
                lstImageItem,//数据来源
                R.layout.item_navi,//item的XML实现

                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
        //添加并且显示
        gridview.setAdapter(saImageItems);

        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(rootView.getContext(),name[position], Toast.LENGTH_LONG).show();
                param = position;
                Log.i(TAG,"Position" + position);
                if (position == 3) {
                    showDialg(view);
                }else {
                    initFrag(view);
                }
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

    private void initFrag(View view) {
        Fragment fragment;
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragment = new YuwenFrag();
        Bundle bundle = new Bundle();
        bundle.putInt("param", param);
        bundle.putString("course",course);
        Log.i(TAG,"param out param=" + param);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }
    
    private void showDialg(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        //builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("复选框");
        builder.setMultiChoiceItems(new String[]{"item1","item2"},null,null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  
                //设置你的操作事项  
            }  
        });  
  
        builder.setNegativeButton("取消",  
                                new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                });  
  
        builder.create().show();  
    }

}

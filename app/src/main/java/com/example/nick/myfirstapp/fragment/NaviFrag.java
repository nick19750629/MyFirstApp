package com.example.nick.myfirstapp.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.subjectOperation;
import com.example.nick.myfirstapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/09.
 */

public class NaviFrag extends Fragment {

    private static final String TAG = "NaviFrag";
    private View rootView;

    private int param;
    private String course;

    private StringBuffer retStr;

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
                if (course.equals(constant.SUB_ENGLISH) && (position == 2)) {
                    Toast.makeText(rootView.getContext(),"skip",Toast.LENGTH_LONG).show();
                    return;
                }
                    Log.i(TAG,"Position" + position);
                retStr = new StringBuffer();
                //UPD 2017/12/20 功能流程简化
                if (getSettingSWInfo(rootView,"example_switch")) {
                    String[] items = getItems(course);
                    final boolean[] checkedItems = getCheckedItems(items.length);
                    showDialg(view,items,checkedItems);
                }else {
                    initFrag(view);
                }
                /*
                if (position == 3) {
                    String[] items = getItems(course);
                    final boolean[] checkedItems = getCheckedItems(items.length);
                    showDialg(view,items,checkedItems);
                }else {
                    initFrag(view);
                }
                */
            }
        });

        /*Toast.makeText(rootView.getContext(),getSettingInfo(rootView,"english_amount"),Toast.LENGTH_LONG).show();
        if (getSettingSWInfo(rootView,"example_switch")) {
            Toast.makeText(rootView.getContext(),"true",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(rootView.getContext(),"false",Toast.LENGTH_LONG).show();
        }*/
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
        bundle.putString("select",retStr.toString());
        Log.i(TAG,"param out param=" + param);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }
    
    private void showDialg(View v,String[] items,final boolean[] checkedItems) {
        //final String[] items = {"item1","item2"};
        //final boolean[] checkedItems = {false,false};
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        //builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("复选框");
        builder.setMultiChoiceItems(items,checkedItems,new DialogInterface.OnMultiChoiceClickListener(){

            @Override
            public void onClick(DialogInterface dialog,int which,boolean isChecked){

            }
        });
        builder.create();
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {
                //Log.i(TAG,"确定 clicked");
                //设置你的操作事项
                for (int i = 0;i < checkedItems.length; i++) {
                    //Log.i(TAG,"checkedItems" + checkedItems[i]);
                    if (checkedItems[i]) {
                        if ("".equals(retStr.toString())) {
                            retStr.append(i);
                        } else {
                            retStr.append(",").append(i);
                        }
                        Log.i(TAG,i + "checked retStr=" + retStr);
                    }
                }
                initFrag(rootView);
                //dialog.dismiss();
            }
        });  
  
        builder.setNegativeButton("取消",  
                                new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {  
                        //dialog.dismiss();
                    }  
                });  
  
        builder.show();
    }

    private String[] getItems(String pSubject) {
        String[] items = {"item0","item1","item2","item3","item4","item5","item6","item7","item8"};
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext(), constant.DB_NAME);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<HashMap<String,String>> modules = subjectOperation.queryBySubject(db,pSubject);
            items = new String[modules.size()];
            for (int i = 0;i < modules.size();i++) {
                items[i] = modules.get(i).get("m");
            }
        }catch (Exception e) {
            Log.i(TAG,"Exception:"+ e.getMessage());
        }
        return items;
    }

    final private boolean[] getCheckedItems(int cnt) {
        boolean[] checkedItems = new boolean[cnt];
        return checkedItems;
    }

    public static String getSettingInfo(View v) {
        SharedPreferences setting  = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        String key = "example_text";

        return setting.getString(key,"empty");
    }

    public static boolean getSettingSWInfo(View v ,String key) {
        SharedPreferences setting  = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        return setting.getBoolean(key,false);
    }

}

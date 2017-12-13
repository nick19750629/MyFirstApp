package com.example.nick.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/10/08.
 */

public class Practice {

    private static final String TAG = "Practice";

    //data
    private List<HashMap<String,String>> lstPractice;
    //control
    private int idx;
    //private static final int SF_MAX = 5;   //4DEBUG 每次练习单词/词组数量 可以根据能力调整
    private static final int SF_MAX = 30;   //4REL 每次练习单词/词组数量 可以根据能力调整

    public Practice(List<HashMap<String,String>> dic){
        lstPractice = new ArrayList<HashMap<String,String>>();

        int max = (dic.size() > SF_MAX ? SF_MAX : dic.size());
        Collections.shuffle(dic);
        for (int i = 0; i < max; i++){
            /*
            int idx = (int)(Math.random() * dic.size());
            HashMap<String, String> word = dic.get(idx);
            */
            HashMap<String, String> word = dic.get(i);
            lstPractice.add(word);
        }
        idx = 0;
    }

    public HashMap<String,String> getFirstWord(){
        Log.i(TAG,"getFirstWord begin");
        //System.out.println("getFirstWord");
        idx = 0;
        HashMap<String,String> word = lstPractice.get(idx);
        return word;
    }

    public HashMap<String,String> getNextWord() {
        Log.i(TAG,"getNextWord begin");
        idx = idx + 1;
        //System.out.println("getNextWord" + idx);
        HashMap<String,String> word = null;
        if (idx < lstPractice.size()) {
            word = lstPractice.get(idx);
        }
        return word;
    }

    public  void restart() {
        idx = -1;
    }

    public boolean isLast() {
        return (idx >= lstPractice.size() );
    }
}

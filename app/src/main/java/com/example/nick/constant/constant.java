package com.example.nick.constant;

import com.example.nick.myfirstapp.R;

/**
 * Created by nick on 2017/10/12.
 */

public class constant {

    public static final String DB_NAME = "dic_db";

    public static final int CNT_ALL = 0;
    public static final int CNT_USED = 1;

    public static final int SUM_CORRECT = 1;
    public static final int SUM_MISTAKE = 0;

    public static final int MAX_DISP = 30;

    ///*
    //4Rel
    public static final String SUB_CHINESE = "语文";
    public static final String SUB_MATH = "数学";
    public static final String SUB_ENGLISH = "英语";

    public static final String SUB_SUGGESTION = "建议";
    public static final String SUB_IDEA = "主意";
    public static final String SUB_BUG = "故障";
    //*/

    /*
    //4Debug
    public static final String SUB_CHINESE = "Chinese";
    public static final String SUB_MATH = "Math";
    public static final String SUB_ENGLISH = "English";

    public static final String SUB_SUGGESTION = "Suggest";
    public static final String SUB_IDEA = "Idea";
    public static final String SUB_BUG = "Bug";

    */

    //定义图标数组
    public static final int[] CImageRes = {
            R.drawable.pinyin,
            R.drawable.hanzi,
            R.drawable.wysw,
            R.drawable.st,
            R.drawable.rand
    };

    //定义图标数组
    public static final int[] EImageRes = {
            R.drawable.c2e,
            R.drawable.e2c,
            R.drawable.senmk,
            R.drawable.st,
            R.drawable.rand
    };

    //定义图标下方的名称数组
    public static final String[] copt_name = {
            "看汉字写拼音",
            "看拼音写汉字",
            "望义生文",
            "随堂练习",
            "随机测试"
    };

    public static final String[] eopt_name = {
            "汉译英",
            "英译汉",
            "造句",
            "随堂练习",
            "随机测试"
    };

}

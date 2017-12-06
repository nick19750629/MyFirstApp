package com.example.nick.util;

/**
 * Created by nick on 2017/10/08.
 */
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class baseUtil {

    public static  int dice(){
        int ret = (int)(Math.random() * 6);
        return ret;
    }

    public static String formatDate(int y,int m,int d) {
        return String.format("%d-%02d-%02d",y,m + 1,d);
    }

    public static String formatTime(int h,int m) {
        return (new StringBuilder().append(h < 10 ? "0" + h : h).append(":")
                .append(m < 10 ? "0" + m : m).append(":00")).toString();
    }

}

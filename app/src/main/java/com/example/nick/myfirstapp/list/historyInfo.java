package com.example.nick.myfirstapp.list;

/**
 * Created by nick on 2017/10/17.
 */

public class historyInfo {
    private int id; //信息ID
    private String title;   //信息标题
    private String details; //详细信息

    //信息ID处理函数
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //标题
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //详细信息
    public void setDetails(String info) {
        this.details = info;
    }
    public String getDetails() {
        return details;
    }

}

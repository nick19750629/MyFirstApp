package com.example.nick.myfirstapp.list;

/**
 * Created by nick on 2017/10/17.
 */

public class subjectInfo {
    private int id; //信息ID
    private String title;   //信息标题
    private int progress; //进度
    private int max;    //最大值
    private int avatar; //图片ID
    private String subject; //课程
    private String version; //版本

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

    //课程
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    //版本
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    //进度
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public int getProgress() {
        return progress;
    }

    //最大值
    public void setMax(int max) {
        this.max = max;
    }
    public int getMax() {
        return max;
    }

    //图片
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getAvatar() {
        return avatar;
    }
}

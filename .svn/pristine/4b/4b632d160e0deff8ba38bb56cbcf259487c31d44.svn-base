package com.example.nick.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/09/29.
 */
public class habitOperation {

    private static  String TAG = "habitOperation";
    private  static String table_name = "habit";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("subject text,title text,start datetime,end datetime,");
        sql.append("minutes integer,correctrate double,comment text,flag integer)");

        Log.i(TAG,"create habit");
        db.execSQL(sql.toString());
    }

    public static void dropTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("drop table if exists ").append(table_name);

        Log.i(TAG,"drop habit");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values.put("subject",(String)rec.get("s"));
        values.put("title",(String)rec.get("t"));
        values.put("start", dbUtil.transDbDateType(rec.get("sd")));
        values.put("end",0);
        values.put("minutes",0);
        values.put("correctrate",0);
        values.put("comment",(String)rec.get("c"));
        values.put("flag",0);

        Log.i(TAG,"insert habit");
        db.insert(table_name,null,values);
    }

    public static void update(SQLiteDatabase db,int id,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        if (rec.get("sd") != null) {
            updValues.put("start", dbUtil.transDbDateType(rec.get("sd")));
        }
        if (rec.get("ed") != null) {
            updValues.put("end", dbUtil.transDbDateType(rec.get("ed")));
        }
        if (rec.get("m") != null) {
            updValues.put("minutes" ,(String)rec.get("m"));
        }
        if (rec.get("rt") != null) {
            updValues.put("correctrate" ,(String)rec.get("rt"));
        }
        if (rec.get("f") != null) {
            updValues.put("flag" ,(int)rec.get("f"));
        }
        updValues.put("comment" ,(String)rec.get("c"));

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static void update(SQLiteDatabase db,int id,Date date) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        updValues.put("end" ,dbUtil.transDbDateType(date));
        updValues.put("flag" ,1);

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static void close(SQLiteDatabase db,int id) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        updValues.put("flag" ,9);

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static List<HashMap<String,Object>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","subject","title", "start",
               "end","minutes","correctrate","comment","flag"},null,null,null,null,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> word = new HashMap<String,Object>();
            word.put("i",cursor.getInt(0));
            word.put("s",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("sd",dbUtil.transDateType(cursor.getLong(3)));
            word.put("ed",dbUtil.transDateType(cursor.getLong(4)));
            word.put("m",cursor.getInt(5));
            word.put("rt",cursor.getFloat(6));
            word.put("c",cursor.getString(7));
            word.put("f",cursor.getInt(8));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,Object>> queryAllByUnclosed(SQLiteDatabase db) {
        String sqlStatement;

        sqlStatement = "select _id,subject,title,start,end,minutes,correctrate,comment,flag from "
                + table_name + " where flag < 9 ";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> word = new HashMap<String,Object>();
            word.put("i",cursor.getInt(0));
            word.put("s",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("sd",dbUtil.transDateType(cursor.getLong(3)));
            word.put("ed",dbUtil.transDateType(cursor.getLong(4)));
            word.put("m",cursor.getInt(5));
            word.put("rt",cursor.getFloat(6));
            word.put("c",cursor.getString(7));
            word.put("f",cursor.getInt(8));
            lstRet.add(word);
        }
        Log.i(TAG,"select * unclosed :"+lstRet.size());
        return lstRet;
    }

    public static String queryDataToFile(SQLiteDatabase db,String path) {
        String fileName = table_name + ".csv";
        Cursor cursor = db.query(table_name,new String[]{"_id","subject","title",
                "start","end","minutes","correctrate","comment","flag"},null,null,null,null,null);
        dbUtil.ExportToCSV(cursor,path,fileName);
        return fileName;
    }

    public static ContentValues query(SQLiteDatabase db,int id) {
        ContentValues values = new ContentValues();
        String sqlStatement;

        sqlStatement = "select _id,subject,title,start,end,minutes,correctrate,comment,flag from "
                + table_name + " where _id = " + id ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("subject",cursor.getString(cursor.getColumnIndex("subject")));
            values.put("title",cursor.getString(cursor.getColumnIndex("title")));
            values.put("start",cursor.getLong(cursor.getColumnIndex("start")));
            values.put("end",cursor.getLong(cursor.getColumnIndex("end")));
            values.put("minutes",cursor.getInt(cursor.getColumnIndex("minutes")));
            values.put("correctrate",cursor.getFloat(cursor.getColumnIndex("correctrate")));
            values.put("comment",cursor.getString(cursor.getColumnIndex("comment")));
            values.put("flag",cursor.getInt(cursor.getColumnIndex("flag")));
        }
        return values;
    }

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }
}

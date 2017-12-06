package com.example.nick.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/09/29.
 */
public class practiceDetailOperation {

    private static  String TAG = "practiceDetailOperation";
    private  static String table_name = "practiceDetail";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("pid integer,question text,answer text,correct integer)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values.put("pid",(Integer) rec.get("p"));
        values.put("question",(String)rec.get("q"));
        values.put("answer",(String)rec.get("a"));
        values.put("correct",1);

        Log.i(TAG,"insert practiceDetail");
        db.insert(table_name,null,values);
    }

    public static void update(SQLiteDatabase db,int id,int correct) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        updValues.put("correct" ,correct);

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static List<HashMap<String,Object>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","pid","question",
                "answer","correct"},null,null,null,null,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> practice = new HashMap<String,Object>();
            practice.put("p",cursor.getInt(1));
            practice.put("q",cursor.getString(2));
            practice.put("a",cursor.getString(3));
            practice.put("c",cursor.getInt(4));
            lstRet.add(practice);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,Object>> queryByPid(SQLiteDatabase db,int pid) {
        String sqlStatement;

        sqlStatement = "select _id,pid,question,answer,correct from "
                + table_name + " where pid = " + pid ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> practice = new HashMap<String,Object>();
            practice.put("i",cursor.getInt(0));
            practice.put("p",cursor.getInt(1));
            practice.put("q",cursor.getString(2));
            practice.put("a",cursor.getString(3));
            practice.put("c",cursor.getInt(4));
            lstRet.add(practice);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static String queryDataToFile(SQLiteDatabase db,String path) {
        String fileName = table_name + ".csv";
        String sqlStatement = "select _id,pid,question,answer,correct from "
                + table_name + " where correct = 1" ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        dbUtil.ExportToCSV(cursor,path,fileName);
        return fileName;
    }

    public static ContentValues query(SQLiteDatabase db,int id) {
        ContentValues values = new ContentValues();
        String sqlStatement;

        sqlStatement = "select _id,pid,question,answer,correct from "
                + table_name + " where _id = " + id ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("pid",cursor.getInt(cursor.getColumnIndex("pid")));
            values.put("question",cursor.getString(cursor.getColumnIndex("question")));
            values.put("answer",cursor.getString(cursor.getColumnIndex("answer")));
            values.put("correct",cursor.getInt(cursor.getColumnIndex("correct")));
        }
        return values;
    }

    /*
    public static long getCountByMode(SQLiteDatabase db,int mode) {
        long lngRet = 0;
        String sqlStatement;

        switch (mode) {
            case 1:
                sqlStatement = "select count(*) from " + table_name + " where correct > 0 or mistake > 0" ;
                break;
            default:
                sqlStatement = "select count(*) from " + table_name;
                break;
        }
        Cursor cursor = db.rawQuery(sqlStatement,null);
        cursor.moveToFirst();
        lngRet = cursor.getLong(0);
        return lngRet;
    }

    public static long getSumByMode(SQLiteDatabase db,int mode) {
        long lngRet = 0;
        String sqlStatement;

        switch (mode) {
            case 1:
                sqlStatement = "select sum(correct) from " + table_name + " where correct > 0" ;
                break;
            default:
                sqlStatement = "select sum(mistake) from " + table_name + " where mistake > 0" ;
                break;
        }
        Cursor cursor = db.rawQuery(sqlStatement,null);
        cursor.moveToFirst();
        lngRet = cursor.getLong(0);
        return lngRet;
    }*/

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }


}

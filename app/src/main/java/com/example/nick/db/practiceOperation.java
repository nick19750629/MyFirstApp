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
public class practiceOperation {

    private static  String TAG = "practiceOperation";
    private  static String table_name = "practice";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("course text,content text,scope text,start datetime,");
        sql.append("minutes integer,score integer)");
        db.execSQL(sql.toString());
    }

    public static long insert(SQLiteDatabase db,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values.put("course",(String)rec.get("c"));
        values.put("content",(String)rec.get("t"));
        values.put("scope",(String)rec.get("s"));
        values.put("start",dbUtil.transDbDateType((Date)rec.get("d")));
        values.put("minutes",0);
        values.put("score",0);

        Log.i(TAG,"insert practice");
        return db.insert(table_name,null,values);
    }

    /*挂起
    public static void insertBatch(SQLiteDatabase db,HashMap<String,String> rec) {
        StringBuffer sqlStatement = new StringBuffer();

        sqlStatement.append("INSERT INTO ").append(table_name);
        sqlStatement.append(" (zici,pinyin,shiyi,origin,correct,mistake) VALUES");
        sqlStatement.append("(\"").append(rec.get("e"));
        sqlStatement.append("\",\"").append(rec.get("p"));
        sqlStatement.append("\",\"").append(rec.get("c"));
        sqlStatement.append("\",\"").append(rec.get("o"));
        sqlStatement.append("\",").append("0");
        sqlStatement.append(",").append("0");
        sqlStatement.append(");");

        db.execSQL(sqlStatement.toString());
    }

    public static void count(SQLiteDatabase db,String key,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values = query(db,rec);

        ContentValues updValues = new ContentValues();
        updValues.put(key ,(Integer) values.get(key) + 1);

        String whereClause = "zici=?";
        String[] whereArgs = {rec.get("e")};
        db.update(table_name,updValues,whereClause,whereArgs);
    }
    */

    public static void updScore(SQLiteDatabase db,int id,int score) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        updValues.put("score" ,score);

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static void updMinutes(SQLiteDatabase db,int id,int m) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        updValues.put("minutes" ,m);

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }


    public static List<HashMap<String,Object>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","course","content",
                "scope","start","minutes","score"},null,null,null,null,"start desc");
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> practice = new HashMap<String,Object>();
            practice.put("i",cursor.getInt(0));
            practice.put("c",cursor.getString(1));
            practice.put("t",cursor.getString(2));
            practice.put("s",cursor.getString(3));
            practice.put("d",dbUtil.transDateType(cursor.getLong(4)));
            practice.put("m",cursor.getInt(5));
            practice.put("r",cursor.getInt(6));
            lstRet.add(practice);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    /*
    public static String queryDataToFile(SQLiteDatabase db,String path) {
        String fileName = table_name + ".csv";
        Cursor cursor = db.query(table_name,new String[]{"_id","zici","pinyin",
                "shiyi","origin"},null,null,null,null,null);
        dbUtil.ExportToCSV(cursor,path,fileName);
        return fileName;
    }
    */

    public static ContentValues query(SQLiteDatabase db,int id) {
        ContentValues values = new ContentValues();
        String sqlStatement;

        sqlStatement = "select _id,course,content,scope,start,minutes,score from "
                + table_name + " where _id = " + id ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("course",cursor.getString(cursor.getColumnIndex("course")));
            values.put("content",cursor.getString(cursor.getColumnIndex("content")));
            values.put("scope",cursor.getString(cursor.getColumnIndex("scope")));
            values.put("start",cursor.getLong(cursor.getColumnIndex("start")));
            values.put("minutes",cursor.getInt(cursor.getColumnIndex("minutes")));
            values.put("score",cursor.getInt(cursor.getColumnIndex("score")));
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

    public static void deleteByKey(SQLiteDatabase db,int id) {
        db.delete(table_name, "_id = ?", new String[] { (new Integer(id)).toString() });
    }
}

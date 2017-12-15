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
public class subjectOperation {

    private static  String TAG = "subjectOperation";
    private  static String table_name = "subject";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("subject text, display_sort integer,module text)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values.put("subject",rec.get("s"));
        values.put("display_sort",rec.get("d"));
        values.put("module",rec.get("m"));

        Log.i(TAG,"insert " + table_name);
        db.insert(table_name,null,values);
    }

    public static void insertBatch(SQLiteDatabase db,HashMap<String,String> rec) {
        StringBuffer sqlStatement = new StringBuffer();

        sqlStatement.append("INSERT INTO ").append(table_name);
        sqlStatement.append(" (subject,display_sort,module) VALUES");
        sqlStatement.append("(\"").append(rec.get("s"));
        sqlStatement.append("\",").append(rec.get("d"));
        sqlStatement.append(",\"").append(rec.get("m"));
        sqlStatement.append("\");");

        db.execSQL(sqlStatement.toString());
    }

    public static List<HashMap<String,String>> queryByProgress(SQLiteDatabase db,String pSubject,int pSort) {
        String sqlStatement;
        sqlStatement = "select _id,subject,display_sort,module from " + table_name + " where subject = '"
                + pSubject + "' and display_sort < " + pSort;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("s",cursor.getString(1));
            word.put("d",cursor.getString(2));
            word.put("m",cursor.getString(3));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }


    public static List<HashMap<String,String>> queryByKey(SQLiteDatabase db,String pSubject,int pSort) {
        String sqlStatement;
        sqlStatement = "select _id,subject,display_sort,module from " + table_name + " where subject = '"
                + pSubject + "' and display_sort = " + pSort;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("s",cursor.getString(1));
            word.put("d",cursor.getString(2));
            word.put("m",cursor.getString(3));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryBySubject(SQLiteDatabase db,String pSubject) {
        String sqlStatement;
        sqlStatement = "select _id,subject,display_sort,module from " + table_name + " where subject = '"
                + pSubject + "' order by display_sort";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("s",cursor.getString(1));
            word.put("d",cursor.getString(2));
            word.put("m",cursor.getString(3));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;    }

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }

    public static long getCount(SQLiteDatabase db) {
        long lngRet = 0;
        String sqlStatement;

        sqlStatement = "select count(*) from " + table_name;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        cursor.moveToFirst();
        lngRet = cursor.getLong(0);
        return lngRet;
    }


}

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
public class progressOperation {

    private static  String TAG = "progressOperation";
    private  static String table_name = "progress";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("subject text, progress integer,sum_progress integer)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values.put("subject",rec.get("s"));
        values.put("progress",rec.get("p"));
        values.put("sum_progress",rec.get("a"));

        Log.i(TAG,"insert " + table_name);
        db.insert(table_name,null,values);
    }

    public static void insertBatch(SQLiteDatabase db,HashMap<String,String> rec) {
        StringBuffer sqlStatement = new StringBuffer();

        sqlStatement.append("INSERT INTO ").append(table_name);
        sqlStatement.append(" (subject,progress,sum_progress) VALUES");
        sqlStatement.append("(\"").append(rec.get("s"));
        sqlStatement.append("\",").append(rec.get("p"));
        sqlStatement.append(",").append(rec.get("a"));
        sqlStatement.append(");");

        db.execSQL(sqlStatement.toString());
    }

    public static List<HashMap<String,Object>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","subject","progress",
                "sum_progress"},null,null,null,null,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> word = new HashMap<String,Object>();
            word.put("i",cursor.getInt(0));
            word.put("s",cursor.getString(1));
            word.put("p",cursor.getInt(2));
            word.put("a",cursor.getInt(3));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryByProgress(SQLiteDatabase db,String pSubject) {
        String sqlStatement;
        sqlStatement = "select _id,subject,display_sort,module from " + table_name + " where subject = '"
                + pSubject + "'";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("s",cursor.getString(1));
            word.put("p",cursor.getString(2));
            word.put("a",cursor.getString(3));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

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


    public static void updProgress(SQLiteDatabase db,String subject,int progress) {
        ContentValues updValues = new ContentValues();
        updValues.put("progress" ,progress);

        String whereClause = "subject=?";
        String[] whereArgs = {String.valueOf(subject)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

}

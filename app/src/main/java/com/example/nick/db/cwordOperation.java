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
public class cwordOperation {

    private static  String TAG = "cwordOperation";
    private  static String table_name = "cword";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("zici text,pinyin text,shiyi text,origin integer,");
        sql.append("correct integer,mistake integer)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values.put("zici",rec.get("e"));
        values.put("pinyin",rec.get("p"));
        values.put("shiyi",rec.get("c"));
        values.put("origin",rec.get("o"));
        values.put("correct",0);
        values.put("mistake",0);

        Log.i(TAG,"insert cword");
        db.insert(table_name,null,values);
    }

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

    public static List<HashMap<String,String>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","zici","pinyin",
                "shiyi","origin"},null,null,null,null,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("e",cursor.getString(1));
            word.put("p",cursor.getString(2));
            word.put("c",cursor.getString(3));
            word.put("o",cursor.getString(4));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryByModule(SQLiteDatabase db,String pModule) {
        String sqlStatement = "select _id,zici,pinyin,shiyi,origin from " + table_name + " where origin = '"
                + pModule + "' ";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("e",cursor.getString(1));
            word.put("p",cursor.getString(2));
            word.put("c",cursor.getString(3));
            word.put("o",cursor.getString(4));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static String queryDataToFile(SQLiteDatabase db,String path) {
        String fileName = table_name + ".csv";
        Cursor cursor = db.query(table_name,new String[]{"_id","zici","pinyin",
                "shiyi","origin"},null,null,null,null,null);
        dbUtil.ExportToCSV(cursor,path,fileName);
        return fileName;
    }

    public static ContentValues query(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(table_name,new String[]{"_id","zici","pinyin",
                "correct","mistake"},"zici=?",new String[]{rec.get("e")},null,null,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("zici",cursor.getString(cursor.getColumnIndex("zici")));
            values.put("pinyin",cursor.getString(cursor.getColumnIndex("pinyin")));
            values.put("correct",cursor.getInt(cursor.getColumnIndex("correct")));
            values.put("mistake",cursor.getInt(cursor.getColumnIndex("mistake")));
        }
        return values;
    }

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
    }

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }


}

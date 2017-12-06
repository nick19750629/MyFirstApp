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
public class improveOperation {

    private static  String TAG = "improveOperation";
    private  static String table_name = "improve";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("subject text,title text,register datetime,");
        sql.append("comment text,flag integer)");

        Log.i(TAG,"create improve");
        db.execSQL(sql.toString());
    }

    public static void dropTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("drop table if exists ").append(table_name);

        Log.i(TAG,"drop improve");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values.put("subject",(String)rec.get("s"));
        values.put("title",(String)rec.get("t"));
        values.put("register", dbUtil.transDbDateType(rec.get("r")));
        values.put("comment",(String)rec.get("c"));
        values.put("flag",0);

        Log.i(TAG,"insert improve");
        db.insert(table_name,null,values);
    }

    public static void update(SQLiteDatabase db,int id,HashMap<String,Object> rec) {
        ContentValues values = new ContentValues();

        values = query(db,id);

        ContentValues updValues = new ContentValues();
        if (rec.get("r") != null) {
            updValues.put("end", dbUtil.transDbDateType(rec.get("r")));
        }
        if (rec.get("f") != null) {
            updValues.put("flag" ,(int)rec.get("f"));
        }
        updValues.put("comment" ,(String)rec.get("c"));

        String whereClause = "_id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static List<HashMap<String,Object>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","subject","title", "register",
               "comment","flag"},null,null,null,null,null);
        List<HashMap<String,Object>> lstRet = new ArrayList<HashMap<String,Object>>();
        while (cursor.moveToNext()){
            HashMap<String,Object> word = new HashMap<String,Object>();
            word.put("i",cursor.getInt(0));
            word.put("s",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("r",dbUtil.transDateType(cursor.getLong(3)));
            word.put("c",cursor.getString(4));
            word.put("f",cursor.getInt(5));
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static ContentValues query(SQLiteDatabase db,int id) {
        ContentValues values = new ContentValues();
        String sqlStatement;

        sqlStatement = "select _id,subject,title,register,comment,flag from "
                + table_name + " where _id = " + id ;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("subject",cursor.getString(cursor.getColumnIndex("subject")));
            values.put("title",cursor.getString(cursor.getColumnIndex("title")));
            values.put("register",cursor.getLong(cursor.getColumnIndex("register")));
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

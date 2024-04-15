package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todo.db";
    private static final String TABLE_NAME = "todolist";

    public SqliteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTbStr = "create table if not exists " + TABLE_NAME + "( _id integer primary key, name varchar, age integer, sex varchar)";
        sqLiteDatabase.execSQL(createTbStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);

        String sql = "alter table " + TABLE_NAME + " add job varchar";
        sqLiteDatabase.execSQL(sql);
    }


}

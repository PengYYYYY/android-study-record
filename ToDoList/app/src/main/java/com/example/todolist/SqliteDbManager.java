package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteDbManager
{
    private static SqliteDbManager mInstance = null;
    private SQLiteDatabase mDb = null;
    private SqliteDbHelper mDbHelper = null;

    public static SqliteDbManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new SqliteDbManager();
        }
        return mInstance;
    }

    public void setSqliteDbOpen(Context context)
    {
        mDbHelper = new SqliteDbHelper(context.getApplicationContext());
        mDb = mDbHelper.getWritableDatabase();
    }

    public void insertTb(String tbName)
    {
        openDb();

        //方法一：
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","zhangsan");
        contentValues.put("age",18);
        contentValues.put("sex","男");
        mDb.insert(tbName,null,contentValues);

        //方法二：
        mDb.execSQL("insert into "+tbName+" (name,age,sex) values ('李四',20,'女')");
        mDb.execSQL("insert into "+tbName+" (name,age,sex) values ('王五',22,'女')");
        mDb.execSQL("insert into "+tbName+" (name,age,sex) values ('哈利',21,'男')");
        closeDb();
    }

    public void deleteTb(String tbName)
    {
        openDb();

        //方法一：
        mDb.delete(tbName,"name=?",new String[]{"zhangsan"});

        //方法二：
        mDb.execSQL("delete from "+tbName+" where name = '李四'");
        closeDb();
    }

    public void updateTb(String tbName)
    {
        openDb();

        //方法一：
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","隔壁老王");
        contentValues.put("sex","男");
        mDb.update(tbName,contentValues,"name=?",new String[]{"王五"});

        //方法二：
        mDb.execSQL("update " + tbName + " set name = '哈利波特', age = '16' where name = '哈利'");
        closeDb();
    }

    public void queryTb(String tbName)
    {
        openDb();
        //方法一：
        Cursor cursor = mDb.query(tbName, new String[]{"name","sex","age"}, "name=?", new String[]{"隔壁老王"}, null, null, null);
        //将光标移动到下一行，从而判断该结果集是否还有下一条数据；如果有则返回true，没有则返回false
        if (null != cursor)
        {
            while (cursor.moveToNext())
            {
                int nameIndex = cursor.getColumnIndex("name");
                String name = cursor.getString(nameIndex);

                int ageIndex = cursor.getColumnIndex("age");
                int age = cursor.getInt(ageIndex);

                int sexIndex = cursor.getColumnIndex("sex");
                String sex = cursor.getString(sexIndex);

                Log.i("????","name = " + name + "; age = " + age + "; sex = " + sex);
            }
            cursor.close();
        }
        //方法二：使用sql语句
        Cursor rawQuery = mDb.rawQuery("select * from " + tbName+" where name=?", new String[]{"哈利波特"});
        if (null != rawQuery)
        {
            while (rawQuery.moveToNext())
            {
                int idIndex = rawQuery.getColumnIndex("_id");
                String _id = rawQuery.getString(idIndex);

                int nameIndex = rawQuery.getColumnIndex("name");
                String name = rawQuery.getString(nameIndex);

                int ageIndex = rawQuery.getColumnIndex("age");
                int age = rawQuery.getInt(ageIndex);

                int sexIndex = rawQuery.getColumnIndex("sex");
                String sex = rawQuery.getString(sexIndex);
                Log.i("????","_id = " + _id + "; name = " + name + "; age = " + age + "; sex = " + sex);
            }
            rawQuery.close();
        }
        closeDb();
    }

    /**
     *  创建或打开一个可以读的数据库
     */
    private void openDb() {
        if (this.mDbHelper != null) {
            try {
                mDb = mDbHelper.getWritableDatabase();
            } catch (Exception e) {
                mDb = mDbHelper.getReadableDatabase();
                e.printStackTrace();
            }
        }
    }

    /**
     *  关闭数据库
     */
    private void closeDb() {
        try {
            if (mDb != null) {
                mDb.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


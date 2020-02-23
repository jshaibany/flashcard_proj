package com.example.jandpartners.flashcardspro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.jandpartners.flashcardspro.global.MyApplication;

public class dataAccessLayer {

    private dbAccessHelper mAccessHelper;

    public dataAccessLayer(Context context){

        mAccessHelper=((MyApplication) context.getApplicationContext()).accessHelper;
    }

    public Long insert(String table, ContentValues contentValues) throws SQLiteException {

        return mAccessHelper.getDb().insert(table,null,contentValues);
    }

    public Integer update(String table,ContentValues contentValues,String whereClause,String[] args) throws SQLiteException {

        return mAccessHelper.getDb().update(table,contentValues,whereClause,args);
    }

    public Integer delete(String table,String whereClause,String[] args) throws SQLiteException {

        return mAccessHelper.getDb().delete(table,whereClause,args);
    }

    public void execSQL(String statement,String[] args) throws SQLiteException{

        mAccessHelper.getDb().execSQL(statement,args);
    }

    public Cursor query(String statement) throws SQLiteException{

        return mAccessHelper.getDb().rawQuery(statement,null);
    }

    public Cursor query(String statement, String[] args) throws SQLiteException{

        return mAccessHelper.getDb().rawQuery(statement,args);
    }

    public Cursor query(String table,String[] args,String whereStatement) throws SQLiteException{

        String statement="SELECT * FROM "+table +" "+whereStatement;
        return mAccessHelper.getDb().rawQuery(statement,args);
    }
}

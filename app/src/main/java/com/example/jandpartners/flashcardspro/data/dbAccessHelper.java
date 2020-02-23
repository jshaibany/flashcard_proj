package com.example.jandpartners.flashcardspro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class dbAccessHelper {

    //Both members only allowed to be configured by class construct
    private dbOpenHelper openHelper;
    private SQLiteDatabase db;

    public dbOpenHelper getOpenHelper() {
        return openHelper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public dbAccessHelper(Context context){
        try{
            openHelper = dbOpenHelper.getInstance(context);
            db = openHelper.getWritableDatabase();
        }
        catch (Exception e)
        {
            //Log.d("Database Access Error","Error:" + e.toString());
        }
    }

    public dbAccessHelper(Context context,Boolean ReadOnly){

        if(ReadOnly){

            try{
                openHelper = dbOpenHelper.getInstance(context);
                db = openHelper.getReadableDatabase();
            }
            catch (Exception e)
            {
                //Log.d("Database Access Error","Error:" + e.toString());
            }
        }
        else {

            try{
                openHelper = dbOpenHelper.getInstance(context);
                db = openHelper.getWritableDatabase();
            }
            catch (Exception e)
            {
                //Log.d("Database Access Error","Error:" + e.toString());
            }
        }

    }

    public void closeDB(){
        this.db.close();
    }
}

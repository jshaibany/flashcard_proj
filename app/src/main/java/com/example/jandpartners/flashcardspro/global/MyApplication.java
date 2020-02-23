package com.example.jandpartners.flashcardspro.global;

import android.app.Application;
import android.content.Context;

import com.example.jandpartners.flashcardspro.BuildConfig;
import com.example.jandpartners.flashcardspro.data.dbAccessHelper;

public class MyApplication extends Application {

    public dbAccessHelper accessHelper;

    public void initializeDBAccessHelper(Context context){

        if(this.accessHelper==null)
            this.accessHelper = new dbAccessHelper(context);
    }

    public void onCreate() {
        super.onCreate();

        //Timber
        if (BuildConfig.DEBUG) {
            //Timber.plant(new DebugTree());
        }
    }

}

package com.example.jandpartners.flashcardspro.data;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.global.fileUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class dbOpenHelper extends SQLiteOpenHelper {


    private static final String TAG = dbOpenHelper.class.getSimpleName();

    private static final String DB_NAME = "fc_db";
    public static final String DB_FILE_NAME = "/data/data/com.example.jandpartners.flashcardspro/databases/fc_db";
    private static final int DB_VERSION = 1;
    private static dbOpenHelper mInstance = null;
    private Context ctx;

    public dbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;

    }

    public static String getDBName(){

        return DB_NAME;
    }

    public static dbOpenHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new dbOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        readAndExecuteSQLScript(db, ctx, R.raw.fc_db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // need to implement here the upgrade code for future application & database upgrades
        for (int i = oldVersion; i < newVersion; ++i) {
            String migrationName = String.format(Locale.getDefault(),"from_%d_to_%d", i, (i + 1));
            //Log.d(TAG, "Looking for migration file: " + migrationName);
            int migrationFileResId = ctx.getResources()
                    .getIdentifier(migrationName, "raw", ctx.getPackageName());

            if (migrationFileResId != 0) {
                // execute script
                //Log.d(TAG, "Found, executing");
                readAndExecuteSQLScript(db, ctx, migrationFileResId);

            } else {
                //Log.d(TAG, "Not Found");
            }
        }
    }


    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, Integer sqlScriptResId) {
        Resources res = ctx.getResources();

        try {
            InputStream is = res.openRawResource(sqlScriptResId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            executeSQLScript(db, reader);

            reader.close();
            is.close();

        } catch (IOException e) {
            throw new RuntimeException("Unable to read SQL script", e);
        }
    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }

    public boolean importDatabase(String dbPath, Application appContext) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();

        //((MyApplication) appContext).accessHelper.getDb().close();

        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILE_NAME);
        if (newDb.exists()) {
            fileUtilities.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            //((MyApplication) appContext).accessHelper.getDb().close();
            return true;
        }
        return false;
    }
}

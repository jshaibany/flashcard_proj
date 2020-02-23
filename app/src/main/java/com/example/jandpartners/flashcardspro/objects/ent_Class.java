package com.example.jandpartners.flashcardspro.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import androidx.annotation.Nullable;

import com.example.jandpartners.flashcardspro.data.CURD_STATUS;
import com.example.jandpartners.flashcardspro.data.dataAccessLayer;
import com.example.jandpartners.flashcardspro.global.globalDateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ent_Class extends entity implements IManageable,ICascadeDeleteable {

    private List<ent_Section> classSections;

    public List<ent_Section> getClassSections() {
        return classSections;
    }
    public void setClassSections(List<ent_Section> classSections) {
        this.classSections = classSections;
    }

    private Context context;
    private dataAccessLayer dataAccessHelper;

    private void setContext(Context context){

        this.context=context;
    }
    private void setAccessHelper(){

        this.dataAccessHelper=new dataAccessLayer(this.context);
    }

    private String lastActionStatus;

    public String getLastActionStatus() {
        return lastActionStatus;
    }

    public ent_Class(Context context){
        //Construct

        setContext(context);
        setAccessHelper();
    }

    public Cursor getAllClasses(){

        String statement = String.format("SELECT * FROM %s",dataLayer.TABLE_NAME);

        return dataAccessHelper.query(statement);

    }

    @Override
    public void CascadeDelete(){


        List<ent_Section> sections = findClassSections();

        if(sections!=null){

            for (ent_Section s : sections) {

                s.CascadeDelete();
            }
        }

        this.delete();
    }

    @Nullable
    private List<ent_Section> findClassSections(){

        List<ent_Section> result = new ArrayList<>();
        ent_Section ent_section = new ent_Section(context);
        Cursor c = ent_section.getAllSections(getName());

        if(c != null){
            if(c.getCount()>0){

                while (c.moveToNext()){

                    ent_Section s = new ent_Section(context);
                    s.setSectionClass(this);
                    s.setId(c.getInt(c.getColumnIndex(ent_Section.dataLayer.COLUMN_ID)));
                    s.setName(c.getString(c.getColumnIndex(ent_Section.dataLayer.COLUMN_NAME)));
                    s.setCreatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(ent_Section.dataLayer.COLUMN_CREATED_ON))));
                    s.setUpdatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(ent_Section.dataLayer.COLUMN_UPDATED_ON))));

                    result.add(s);
                }

                c.close();

                return result;
            }
        }

        return null;

    }

    @Override
    public void get() {

        String statement = String.format("SELECT * FROM %s WHERE %s=? ",dataLayer.TABLE_NAME,dataLayer.COLUMN_NAME);
        String[] args= {getName()};

        try{

            Cursor c = dataAccessHelper.query(statement,args);

            if(c != null){
                if(c.getCount()>0){

                    c.moveToFirst();

                    setId(c.getInt(c.getColumnIndex(dataLayer.COLUMN_ID)));
                    setName(c.getString(c.getColumnIndex(dataLayer.COLUMN_NAME)));
                    setCreatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_CREATED_ON))));
                    setUpdatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_UPDATED_ON))));

                    c.close();
                }
            }

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.READ_OK);
        }
        catch (Exception e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }
    }

    @Override
    public void create() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(dataLayer.COLUMN_NAME,getName());
        contentValues.put(dataLayer.COLUMN_CREATED_ON,globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));
        contentValues.putNull(dataLayer.COLUMN_UPDATED_ON);

        try{

            dataAccessHelper.insert(dataLayer.TABLE_NAME,contentValues);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.CREATED_OK);
        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.CREATED_FAILED);

        }

    }

    @Override
    public void update() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(dataLayer.COLUMN_NAME,getName());
        contentValues.put(dataLayer.COLUMN_UPDATED_ON,globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

        String whereClause="name=?";
        String[] args={getName()};
        try{

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_FAILED);

        }
    }

    @Override
    public void delete() {

        String whereClause="name=?";
        String[] args={getName()};

        try{

            dataAccessHelper.delete(dataLayer.TABLE_NAME,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }

    public void changeName(String oldCID,String newCID) {


        try{

            ContentValues contentValues = new ContentValues();

            contentValues.put(dataLayer.COLUMN_NAME,newCID);
            contentValues.put(dataLayer.COLUMN_UPDATED_ON,globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

            String whereClause=String.format("%s=?",dataLayer.COLUMN_NAME);
            String[] args={oldCID};

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_Section.dataLayer.COLUMN_FK_NAME,newCID);

            whereClause=String.format("%s=?",ent_Section.dataLayer.COLUMN_FK_NAME);

            dataAccessHelper.update(ent_Section.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_DeckSet.dataLayer.COLUMN_FK_CLASS,newCID);

            whereClause=String.format("%s=?",ent_DeckSet.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_DeckSet.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_FlashCard.dataLayer.COLUMN_FK_CLASS,newCID);

            whereClause=String.format("%s=?",ent_FlashCard.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_FlashCard.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_Quiz.dataLayer.COLUMN_FK_CLASS,newCID);

            whereClause=String.format("%s=?",ent_Quiz.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_Quiz.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_FAILED);

        }
    }

    public static class dataLayer{

        public static String TABLE_NAME="CLASSES";

        public static String COLUMN_ID="_id";
        public static String COLUMN_NAME="name";
        public static String COLUMN_CREATED_ON="created_on";
        public static String COLUMN_UPDATED_ON="updated_on";
    }
}

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

public class ent_Section extends entity implements IManageable,ICascadeDeleteable {

    private ent_Class sectionClass;
    private List<ent_DeckSet> sectionDeckSets;

    public ent_Class getSectionClass(){return sectionClass;}
    public void setSectionClass(ent_Class sectionClass){this.sectionClass=sectionClass;}
    public List<ent_DeckSet> getSectionDeckSets() {
        return sectionDeckSets;
    }

    public void setSectionDecksets(List<ent_DeckSet> sectionDeckSets) {
        this.sectionDeckSets = sectionDeckSets;
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

    public ent_Section(Context context){

        setContext(context);
        setAccessHelper();
    }

    public Cursor getAllSections(String classID){

        String statement = String.format("SELECT * FROM %s WHERE %s=? ",dataLayer.TABLE_NAME,dataLayer.COLUMN_FK_NAME);
        String[] args = {classID};
        return dataAccessHelper.query(statement,args);

    }

    @Override
    public void get() {

        String statement = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",dataLayer.TABLE_NAME,dataLayer.COLUMN_FK_NAME,dataLayer.COLUMN_NAME);
        String[] args= {getSectionClass().getName(),getName()};

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
        contentValues.put(dataLayer.COLUMN_FK_NAME,getSectionClass().getName());
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

        String whereClause=String.format("%s=? AND %s=?",dataLayer.COLUMN_NAME,dataLayer.COLUMN_FK_NAME);
        String[] args={getName(),getSectionClass().getName()};

        try{

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_FAILED);

        }
    }

    public void changeName(String CID,String oldSID,String newSID) {



        try{

            ContentValues contentValues = new ContentValues();

            contentValues.put(dataLayer.COLUMN_NAME,newSID);
            contentValues.put(dataLayer.COLUMN_UPDATED_ON,globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

            String whereClause=String.format("%s=? AND %s=?",dataLayer.COLUMN_NAME,dataLayer.COLUMN_FK_NAME);
            String[] args={oldSID,CID};

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_DeckSet.dataLayer.COLUMN_FK_SECTION,newSID);

            whereClause=String.format("%s=? AND %s=?",ent_DeckSet.dataLayer.COLUMN_FK_SECTION,ent_DeckSet.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_DeckSet.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_FlashCard.dataLayer.COLUMN_FK_SECTION,newSID);

            whereClause=String.format("%s=? AND %s=?",ent_FlashCard.dataLayer.COLUMN_FK_SECTION,ent_FlashCard.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_FlashCard.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            contentValues = new ContentValues();
            contentValues.put(ent_Quiz.dataLayer.COLUMN_FK_SECTION,newSID);

            whereClause=String.format("%s=? AND %s=?",ent_Quiz.dataLayer.COLUMN_FK_SECTION,ent_Quiz.dataLayer.COLUMN_FK_CLASS);

            dataAccessHelper.update(ent_Quiz.dataLayer.TABLE_NAME,contentValues,whereClause,args);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.UPDATE_FAILED);

        }
    }

    @Override
    public void delete() {

        String whereClause=String.format("%s=? AND %s=?",dataLayer.COLUMN_NAME,dataLayer.COLUMN_FK_NAME);
        String[] args={getName(),getSectionClass().getName()};

        try{

            dataAccessHelper.delete(dataLayer.TABLE_NAME,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }

    @Override
    public void CascadeDelete(){

        ent_DeckSet d = new ent_DeckSet(context);
        d.setDecksetClass(this.getSectionClass());
        d.setDecksetSection(this);
        d.CascadeDelete();

        this.delete();

    }



    public static class dataLayer{

        public static String TABLE_NAME="SECTIONS";

        public static String COLUMN_ID="_id";
        public static String COLUMN_NAME="section_name";
        public static String COLUMN_FK_NAME="class_name";
        public static String COLUMN_CREATED_ON="created_on";
        public static String COLUMN_UPDATED_ON="updated_on";
    }


}

package com.example.jandpartners.flashcardspro.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.jandpartners.flashcardspro.data.CURD_STATUS;
import com.example.jandpartners.flashcardspro.data.dataAccessLayer;
import com.example.jandpartners.flashcardspro.global.globalDateFormatter;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ent_DeckSet extends entity implements IManageable,ICascadeDeleteable {

    private ent_Class decksetClass;
    private ent_Section decksetSection;
    private List<ent_FlashCard> flashCards;

    public ent_Class getDecksetClass(){return decksetClass;}
    public void setDecksetClass(ent_Class decksetClass){this.decksetClass=decksetClass;}
    public ent_Section getDecksetSection(){return decksetSection;}
    public void setDecksetSection(ent_Section decksetSection){this.decksetSection=decksetSection;}

    public List<ent_FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<ent_FlashCard> flashCards) {
        this.flashCards = flashCards;
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

    public ent_DeckSet(Context context){

        setContext(context);
        setAccessHelper();
    }

    public Integer getCount(){

        Integer result=0;

        String[] args={getName(),getDecksetSection().getName(),getDecksetClass().getName()};
        String whereClause=String.format("%s=? AND %s=? AND %s=?",dataLayer.COLUMN_NAME,dataLayer.COLUMN_FK_SECTION,dataLayer.COLUMN_FK_CLASS);

        try{

            Cursor c = dataAccessHelper.query(dataLayer.TABLE_NAME,args,whereClause);

            if(c != null){

                result = c.getCount();
                c.close();
            }

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.READ_OK);
        }
        catch (Exception e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }

        return result;
    }

    @Override
    public void get() {

        String statement = String.format("SELECT * FROM %s WHERE %s=? AND %s=?"
                ,dataLayer.TABLE_NAME
                ,dataLayer.COLUMN_FK_CLASS
                ,dataLayer.COLUMN_FK_SECTION);
        String[] args= {getDecksetClass().getName(),getDecksetSection().getName()};

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

        contentValues.put(dataLayer.COLUMN_NAME,dataLayer.set_default_name);
        contentValues.put(dataLayer.COLUMN_FK_SECTION,getDecksetSection().getName());
        contentValues.put(dataLayer.COLUMN_FK_CLASS,getDecksetClass().getName());
        contentValues.put(dataLayer.COLUMN_CREATED_ON, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));
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

    }

    @Override
    public void delete() {

        String whereClause=String.format("%s=? AND %s=? AND %s=?",dataLayer.COLUMN_NAME,dataLayer.COLUMN_FK_SECTION,dataLayer.COLUMN_FK_CLASS);
        String[] args={dataLayer.set_default_name,getDecksetSection().getName(),getDecksetClass().getName()};

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

        try{

            Cursor c = new ent_FlashCard(context).getAllFlashCards(this.getName()
                    ,this.getDecksetSection().getName()
                    ,this.getDecksetClass().getName());

            if(c!=null && c.getCount()>0){

                while (c.moveToNext()){

                    ent_FlashCard f = new ent_FlashCard(context);
                    f.setId(c.getInt(c.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_ID)));
                    f.delete();
                }
            }

            this.delete();
        }
        catch (Exception e){

        }

    }

    public static class dataLayer{

        public static String TABLE_NAME="DECKSETS";
        public static String FLASHCARDS_TABLE="FLASHCARDS";
        public static String set_default_name="مجموعة الكروت التعليمية";

        public static String COLUMN_ID="_id";
        public static String COLUMN_NAME="deckset_name";
        public static String COLUMN_FK_SECTION="section_name";
        public static String COLUMN_FK_CLASS="class_name";
        public static String COLUMN_CREATED_ON="created_on";
        public static String COLUMN_UPDATED_ON="updated_on";
    }
}

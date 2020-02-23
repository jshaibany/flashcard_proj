package com.example.jandpartners.flashcardspro.objects;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.jandpartners.flashcardspro.data.CURD_STATUS;
import com.example.jandpartners.flashcardspro.data.dataAccessLayer;

import javax.crypto.Cipher;

public class proc_Review {

    private Cursor data;

    private Context context;
    private dataAccessLayer dataAccessHelper;
    private String CID;
    private String SID;
    private String DID;
    private String[] currentTerm_Def;

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

    public proc_Review(Context context,String CID,String SID,String DID){

        setContext(context);
        setAccessHelper();
        this.CID = CID;
        this.SID = SID;
        this.DID = DID;

    }

    public void getSequencialSet(final Integer limit){

        String statement;
        String[] args={CID,SID,DID};

        if(limit > 0){

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? LIMIT %s"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,String.valueOf(limit));
        }
        else {

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=?"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET);
        }

        setDataCursor(statement,args);


    }

    public void getRandomSet(final Integer limit){

        String statement;
        String[] args={CID,SID,DID};

        if(limit > 0){

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY random() LIMIT %s"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,String.valueOf(limit));
        }
        else {

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY random()"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET);
        }

        setDataCursor(statement,args);


    }

    public void getByImportanceSet(final Integer limit){

        String statement;
        String[] args={CID,SID,DID};

        if(limit > 0){

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY %s DESC, %s ASC LIMIT %s"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_IMPORTANCE
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,String.valueOf(limit));
        }
        else {

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY %s DESC, %s ASC "
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_IMPORTANCE
                    ,ent_FlashCard.dataLayer.COLUMN_TERM);
        }

        setDataCursor(statement,args);


    }

    public void getByHardnessSet(final Integer limit){

        String statement;
        String[] args={CID,SID,DID};

        if(limit > 0){

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY %s DESC, %s ASC LIMIT %s"
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_HARDNESS
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,String.valueOf(limit));
        }
        else {

            statement =String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? ORDER BY %s DESC, %s ASC "
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_HARDNESS
                    ,ent_FlashCard.dataLayer.COLUMN_TERM);
        }

        setDataCursor(statement,args);


    }

    private void setDataCursor(String statement,String[] args){

        try{

            data=dataAccessHelper.query(statement,args);

            if(data!=null){
                if(data.getCount()>0){

                    lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_OK);

                    return;

                }
            }

            lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }
        catch (Exception e){

            lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }
    }

    public boolean isFirst(){

        return data.isFirst();
    }

    public boolean isLast(){

        return data.isLast();
    }

    public Integer getCurrentPosition(){

        return data.getPosition();
    }

    public Integer getCount(){

        return data.getCount();
    }

    public String[] getCurrentTerm_Def(){

        return currentTerm_Def;
    }

    public void finishProcess(){

        data.close();
    }

    @Nullable
    public String[] getNext(){

        String[] values;

        if(data!=null && data.getCount()>0){

            if(data.getCount()==1) data.moveToFirst();
            if(data.getCount()>1 && !data.isLast()) data.moveToNext();

            values=new String[2];
            values[0] = data.getString(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_TERM));
            values[1] = data.getString(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_DEFINITION));
            currentTerm_Def =values;

            updateFlashCardStats(data.getInt(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_ID)));

            return values;
        }
        else return null;

    }

    public String[] getPrev(){

        String[] values;

        if(data!=null && data.getCount()>0){

            if(data.getCount()==1) data.moveToFirst();
            if(data.getCount()>1 && !data.isFirst()) data.moveToPrevious();

            values=new String[2];
            values[0] = data.getString(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_TERM));
            values[1] = data.getString(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_DEFINITION));
            currentTerm_Def =values;

            updateFlashCardStats(data.getInt(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_ID)));

            return values;
        }
        else return null;

    }

    private void updateFlashCardStats(final Integer id){

        final ent_FlashCard fc = new ent_FlashCard(context);
        fc.setId(id);
        fc.get();
        fc.increaseReviews();

    }


}

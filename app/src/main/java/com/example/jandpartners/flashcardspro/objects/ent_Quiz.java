package com.example.jandpartners.flashcardspro.objects;

import android.content.ContentValues;
import android.content.Context;

import com.example.jandpartners.flashcardspro.data.dataAccessLayer;
import com.example.jandpartners.flashcardspro.global.globalDateFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ent_Quiz {

    private Integer id;
    private String CID;
    private String SID;
    private String DID;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private String result;
    private Date createdOn;

    private Context context;
    private dataAccessLayer dataAccessHelper;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public String getResult() {
        return result;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

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

    public ent_Quiz(Context context,String CID,String SID,String DID){

        setContext(context);
        setAccessHelper();
        this.CID = CID;
        this.SID = SID;
        this.DID = DID;

        create();
    }

    private void create(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(dataLayer.COLUMN_FK_CLASS,CID);
        contentValues.put(dataLayer.COLUMN_FK_SECTION,SID);
        contentValues.put(dataLayer.COLUMN_FK_DECKSET,DID);
        contentValues.put(dataLayer.COLUMN_TOTAL_QUESTIONS,0);
        contentValues.put(dataLayer.COLUMN_CORRECT_ANSWERS,0);
        contentValues.put(dataLayer.COLUMN_CREATED_ON, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

        try{

            Long i =dataAccessHelper.insert(dataLayer.TABLE_NAME,contentValues);

            if(i>0) setId(Integer.decode(String.valueOf(i)));
            else setId(-1);
        }
        catch (Exception e){


        }
    }

    public void update(Integer totalQuestions,Integer correctAnswers){

        String s="%";
        String result = String.format(Locale.ENGLISH,"%s%d",s,Math.round(correctAnswers/totalQuestions));

        ContentValues contentValues = new ContentValues();

        contentValues.put(dataLayer.COLUMN_RESULT,result);
        contentValues.put(dataLayer.COLUMN_TOTAL_QUESTIONS,0);
        contentValues.put(dataLayer.COLUMN_CORRECT_ANSWERS,0);

        String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);
        String[] args = {getId().toString()};

        try{

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);

        }
        catch (Exception e){


        }
    }

    public static class dataLayer{

        public static String TABLE_NAME="QUIZ";

        public static String COLUMN_ID="_id";
        public static String COLUMN_FK_DECKSET="did";
        public static String COLUMN_FK_SECTION="sid";
        public static String COLUMN_FK_CLASS="cid";
        public static String COLUMN_TOTAL_QUESTIONS="total_questions";
        public static String COLUMN_CORRECT_ANSWERS="correct_answers";
        public static String COLUMN_RESULT="result";
        public static String COLUMN_CREATED_ON="created_on";

    }
}

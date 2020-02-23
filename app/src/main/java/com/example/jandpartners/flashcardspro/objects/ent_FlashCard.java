package com.example.jandpartners.flashcardspro.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.jandpartners.flashcardspro.data.CURD_STATUS;
import com.example.jandpartners.flashcardspro.data.dataAccessLayer;
import com.example.jandpartners.flashcardspro.global.globalDateFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ent_FlashCard extends entity implements IManageable {

    private String definition;
    private Integer sequence;
    private Integer importance;
    private Integer hardness;
    private Integer reviewHits;
    private Integer quizHits;
    private Date lastReview;
    private Date lastQuiz;

    private ent_DeckSet flashcardDeckset;
    private ent_Section flashcardSection;
    private ent_Class flashcardClass;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public Integer getHardness() {
        return hardness;
    }

    public void setHardness(Integer hardness) {
        this.hardness = hardness;
    }

    public Integer getReviewHits() {
        return reviewHits;
    }

    public void setReviewHits(Integer reviewHits) {
        this.reviewHits = reviewHits;
    }

    public Integer getQuizHits() {
        return quizHits;
    }

    public void setQuizHits(Integer quizHits) {
        this.quizHits = quizHits;
    }

    public ent_DeckSet getFlashcardDeckset() {
        return flashcardDeckset;
    }

    public void setFlashcardDeckset(ent_DeckSet flashcardDeckset) {
        this.flashcardDeckset = flashcardDeckset;
    }

    public ent_Section getFlashcardSection() {
        return flashcardSection;
    }

    public void setFlashcardSection(ent_Section flashcardSection) {
        this.flashcardSection = flashcardSection;
    }

    public ent_Class getFlashcardClass() {
        return flashcardClass;
    }

    public void setFlashcardClass(ent_Class flashcardClass) {
        this.flashcardClass = flashcardClass;
    }

    public Date getLastReview() {
        return lastReview;
    }

    public void setLastReview(Date lastReview) {
        this.lastReview = lastReview;
    }

    public Date getLastQuiz() {
        return lastQuiz;
    }

    public void setLastQuiz(Date lastQuiz) {
        this.lastQuiz = lastQuiz;
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

    public ent_FlashCard(Context context){

        setContext(context);
        setAccessHelper();
    }

    public Cursor getAllFlashCards(String DID,String SID, String CID){

        String statement = String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=?",dataLayer.TABLE_NAME,dataLayer.COLUMN_FK_DECKSET,dataLayer.COLUMN_FK_SECTION,dataLayer.COLUMN_FK_CLASS);
        String[] args={DID,SID,CID};

        return dataAccessHelper.query(statement,args);

    }
    public List<ent_FlashCard> getFlashCards(){

        List<ent_FlashCard> result = new ArrayList<>();

        String statement = String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=?",dataLayer.TABLE_NAME,dataLayer.COLUMN_FK_DECKSET,dataLayer.COLUMN_FK_SECTION,dataLayer.COLUMN_FK_CLASS);
        String[] args={getFlashcardDeckset().getName(),getFlashcardSection().getName(),getFlashcardClass().getName()};

        try{

            Cursor c = dataAccessHelper.query(statement,args);

            if(c != null){
                if(c.getCount()>0){

                    while (c.moveToNext()){

                        ent_FlashCard flashCard = new ent_FlashCard(context);
                        flashCard.setId(c.getInt(c.getColumnIndex(dataLayer.COLUMN_ID)));
                        flashCard.get();

                        result.add(flashCard);
                    }

                    c.close();
                }
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

        String statement = String.format("SELECT * FROM %s WHERE %s=?",dataLayer.TABLE_NAME,dataLayer.COLUMN_ID);
        String[] args= {String.format(Locale.ENGLISH,"%d",getId())};

        try{

            Cursor c = dataAccessHelper.query(statement,args);

            if(c != null){
                if(c.getCount()>0){

                    c.moveToFirst();

                    setId(c.getInt(c.getColumnIndex(dataLayer.COLUMN_ID)));
                    setName(c.getString(c.getColumnIndex(dataLayer.COLUMN_TERM)));
                    setDefinition(c.getString(c.getColumnIndex(dataLayer.COLUMN_DEFINITION)));
                    setImportance(c.getInt(c.getColumnIndex(dataLayer.COLUMN_IMPORTANCE)));
                    setHardness(c.getInt(c.getColumnIndex(dataLayer.COLUMN_HARDNESS)));
                    setReviewHits(c.getInt(c.getColumnIndex(dataLayer.COLUMN_REVIEW_HITS)));
                    setQuizHits(c.getInt(c.getColumnIndex(dataLayer.COLUMN_QUIZ_HITS)));
                    setCreatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_CREATED_ON))));
                    setUpdatedOn(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_UPDATED_ON))));
                    setLastReview(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_LAST_REVIEW))));
                    setLastQuiz(globalDateFormatter.convertStringToDate(c.getString(c.getColumnIndex(dataLayer.COLUMN_LAST_QUIZ))));

                    final ent_Class ent_class = new ent_Class(context);
                    ent_class.setName(c.getString(c.getColumnIndex(dataLayer.COLUMN_FK_CLASS)));
                    ent_class.get();

                    final ent_Section ent_section = new ent_Section(context);
                    ent_section.setName(c.getString(c.getColumnIndex(dataLayer.COLUMN_FK_SECTION)));
                    ent_section.setSectionClass(ent_class);
                    ent_section.get();

                    final ent_DeckSet ent_deckSet = new ent_DeckSet(context);
                    ent_deckSet.setName(c.getString(c.getColumnIndex(dataLayer.COLUMN_FK_DECKSET)));
                    ent_deckSet.setDecksetClass(ent_class);
                    ent_deckSet.setDecksetSection(ent_section);
                    ent_deckSet.get();

                    setFlashcardClass(ent_class);
                    setFlashcardSection(ent_section);
                    setFlashcardDeckset(ent_deckSet);

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

        contentValues.put(dataLayer.COLUMN_TERM,getName());
        contentValues.put(dataLayer.COLUMN_DEFINITION,getDefinition());
        contentValues.put(dataLayer.COLUMN_FK_DECKSET,getFlashcardDeckset().getName());
        contentValues.put(dataLayer.COLUMN_FK_SECTION,getFlashcardSection().getName());
        contentValues.put(dataLayer.COLUMN_FK_CLASS,getFlashcardClass().getName());
        contentValues.put(dataLayer.COLUMN_IMPORTANCE,getImportance());
        contentValues.put(dataLayer.COLUMN_HARDNESS,getHardness());
        contentValues.put(dataLayer.COLUMN_REVIEW_HITS,0);
        contentValues.put(dataLayer.COLUMN_QUIZ_HITS,0);
        contentValues.put(dataLayer.COLUMN_CREATED_ON, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));
        contentValues.putNull(dataLayer.COLUMN_UPDATED_ON);
        contentValues.putNull(dataLayer.COLUMN_LAST_REVIEW);
        contentValues.putNull(dataLayer.COLUMN_LAST_QUIZ);


        try{

            dataAccessHelper.insert(dataLayer.TABLE_NAME,contentValues);

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.CREATED_OK);
        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.CREATED_FAILED);

        }
    }

    public void increaseReviews(){

        try{

            ContentValues contentValues = new ContentValues();

            contentValues.put(dataLayer.COLUMN_REVIEW_HITS,getReviewHits()+1);
            contentValues.put(dataLayer.COLUMN_LAST_REVIEW, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

            String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);
            String[] args={String.format(Locale.ENGLISH,"%d",getId())};

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }

    public void increaseQuiz(){



        try{

            ContentValues contentValues = new ContentValues();

            contentValues.put(dataLayer.COLUMN_QUIZ_HITS,getQuizHits()+1);
            contentValues.put(dataLayer.COLUMN_LAST_QUIZ, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

            String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);
            String[] args={String.format(Locale.ENGLISH,"%d",getId())};

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }

    @Override
    public void update() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(dataLayer.COLUMN_TERM,getName());
        contentValues.put(dataLayer.COLUMN_DEFINITION,getDefinition());
        contentValues.put(dataLayer.COLUMN_IMPORTANCE,getImportance());
        contentValues.put(dataLayer.COLUMN_HARDNESS,getHardness());
        contentValues.put(dataLayer.COLUMN_UPDATED_ON, globalDateFormatter.formatDateToString(Calendar.getInstance().getTime()));

        String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);
        String[] args={String.format(Locale.ENGLISH,"%d",getId())};

        try{

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }

    @Override
    public void delete() {

        String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);
        String[] args={String.format(Locale.ENGLISH,"%d",getId())};

        try{

            dataAccessHelper.delete(dataLayer.TABLE_NAME,whereClause,args);
            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_OK);

        }
        catch (SQLiteException e){

            lastActionStatus = CURD_STATUS.get(CURD_STATUS.DELETE_FAILED);

        }
    }



    public static class dataLayer{

        public static String TABLE_NAME="FLASHCARDS";

        public static String COLUMN_ID="_id";
        public static String COLUMN_TERM="term";
        public static String COLUMN_DEFINITION="definition";
        public static String COLUMN_FK_DECKSET="deckset_name";
        public static String COLUMN_FK_SECTION="section_name";
        public static String COLUMN_FK_CLASS="class_name";
        public static String COLUMN_IMPORTANCE="importance";
        public static String COLUMN_HARDNESS="hardness";
        public static String COLUMN_REVIEW_HITS="review_hits";
        public static String COLUMN_QUIZ_HITS="quiz_hits";
        public static String COLUMN_CREATED_ON="created_on";
        public static String COLUMN_UPDATED_ON="updated_on";
        public static String COLUMN_LAST_REVIEW="last_review";
        public static String COLUMN_LAST_QUIZ="last_quiz";

    }


}

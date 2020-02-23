package com.example.jandpartners.flashcardspro.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.jandpartners.flashcardspro.data.CURD_STATUS;
import com.example.jandpartners.flashcardspro.data.dataAccessLayer;

import java.util.Locale;

public class proc_Quiz {

    private Cursor data;
    private ent_Quiz quiz;
    private Context context;
    private dataAccessLayer dataAccessHelper;
    private String CID;
    private String SID;
    private String DID;
    private String[] currentTerm_Def;
    private boolean isDone;

    public ent_Quiz getQuiz() {
        return quiz;
    }

    private void setQuiz(ent_Quiz quiz) {
        this.quiz = quiz;
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

    public proc_Quiz(Context context,String CID,String SID,String DID){

        setContext(context);
        setAccessHelper();
        this.CID = CID;
        this.SID = SID;
        this.DID = DID;

        setQuiz(new ent_Quiz(context,CID,SID,DID));

        isDone=false;

    }

    private void setDataCursor(){

        String statement = String.format("SELECT * FROM %s WHERE %s=?"
                ,dataLayer.TABLE_NAME
                ,dataLayer.COLUMN_QUIZ_ID);
        String[] args={getQuiz().getId().toString()};

        try{

            data=dataAccessHelper.query(statement,args);

            if(data!=null){
                if(data.getCount()>0){

                    lastActionStatus= CURD_STATUS.get(CURD_STATUS.READ_OK);

                    return;

                }
            }

            lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }
        catch (Exception e){

            lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }
    }

    @Nullable
    private Cursor getTransitDataCursor(String statement,String[] args){

        try{

            return dataAccessHelper.query(statement,args);
        }
        catch (Exception e){

            lastActionStatus=CURD_STATUS.get(CURD_STATUS.READ_FAILED);
        }

        return null;
    }

    public void getSequencialSet(final Integer limit){

        /*
        INSERT INTO QUIZ_PROCESS('quiz_id','term_id','term')
        SELECT 1,_id,term
        FROM FLASHCARDS t
        WHERE t.class_name=1
        AND t.section_name=1
        AND t.deckset_name=1
        LIMIT 20
         */

        //Insert first part of the quiz (quiz_id,term_id,term)
        //This will generate _id in QUIZ_PROCESS Table
        //Then we need to call a function to update QUIZ_PROCESS Table with Random answers
        //Notice that the answers should be a shuffled set from the first part generated in
        //this step

        String statement;
        String[] args={CID,SID,DID};


        if(limit > 0){

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "LIMIT %d";

            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,limit);
        }
        else {

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? ";
            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET);
        }


        dataAccessHelper.execSQL(statement,args);

        setShuffledAnswers();
        setDataCursor();


    }

    public void getRandomSet(final Integer limit){

        /*
        INSERT INTO QUIZ_PROCESS('quiz_id','term_id','term')
        SELECT 1,_id,term
        FROM FLASHCARDS t
        WHERE t.class_name=1
        AND t.section_name=1
        AND t.deckset_name=1
        LIMIT 20
         */

        //Insert first part of the quiz (quiz_id,term_id,term)
        //This will generate _id in QUIZ_PROCESS Table
        //Then we need to call a function to update QUIZ_PROCESS Table with Random answers
        //Notice that the answers should be a shuffled set from the first part generated in
        //this step

        String statement;
        String[] args={CID,SID,DID};


        if(limit > 0){

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY random() "+
                    "LIMIT %d";

            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,limit);
        }
        else {

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY random() ";
            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET);
        }


        dataAccessHelper.execSQL(statement,args);

        setShuffledAnswers();
        setDataCursor();


    }

    public void getByImportanceSet(final Integer limit){

        /*
        INSERT INTO QUIZ_PROCESS('quiz_id','term_id','term')
        SELECT 1,_id,term
        FROM FLASHCARDS t
        WHERE t.class_name=1
        AND t.section_name=1
        AND t.deckset_name=1
        LIMIT 20
         */

        //Insert first part of the quiz (quiz_id,term_id,term)
        //This will generate _id in QUIZ_PROCESS Table
        //Then we need to call a function to update QUIZ_PROCESS Table with Random answers
        //Notice that the answers should be a shuffled set from the first part generated in
        //this step

        String statement;
        String[] args={CID,SID,DID};


        if(limit > 0){

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY %s DESC, %s ASC "+
                    "LIMIT %d";

            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_IMPORTANCE
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,limit);
        }
        else {

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY %s DESC, %s ASC ";
            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_IMPORTANCE
                    ,ent_FlashCard.dataLayer.COLUMN_TERM);
        }


        dataAccessHelper.execSQL(statement,args);

        setShuffledAnswers();
        setDataCursor();


    }

    public void getByHardnessSet(final Integer limit){

        /*
        INSERT INTO QUIZ_PROCESS('quiz_id','term_id','term')
        SELECT 1,_id,term
        FROM FLASHCARDS t
        WHERE t.class_name=1
        AND t.section_name=1
        AND t.deckset_name=1
        LIMIT 20
         */

        //Insert first part of the quiz (quiz_id,term_id,term)
        //This will generate _id in QUIZ_PROCESS Table
        //Then we need to call a function to update QUIZ_PROCESS Table with Random answers
        //Notice that the answers should be a shuffled set from the first part generated in
        //this step

        String statement;
        String[] args={CID,SID,DID};


        if(limit > 0){

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY %s DESC, %s ASC "+
                    "LIMIT %d";

            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_HARDNESS
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,limit);
        }
        else {

            statement="INSERT INTO %s('%s','%s','%s')"+
                    "SELECT %d,%s,%s "+
                    "FROM %s FC "+
                    "WHERE FC.%s=? "+
                    "AND FC.%s=? "+
                    "AND FC.%s=? "+
                    "ORDER BY %s DESC, %s ASC ";
            statement =String.format(Locale.ENGLISH,statement
                    ,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ID
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_TERM
                    ,getQuiz().getId()
                    ,ent_FlashCard.dataLayer.COLUMN_ID
                    ,ent_FlashCard.dataLayer.COLUMN_TERM
                    ,ent_FlashCard.dataLayer.TABLE_NAME
                    ,ent_FlashCard.dataLayer.COLUMN_FK_CLASS
                    ,ent_FlashCard.dataLayer.COLUMN_FK_SECTION
                    ,ent_FlashCard.dataLayer.COLUMN_FK_DECKSET
                    ,ent_FlashCard.dataLayer.COLUMN_HARDNESS
                    ,ent_FlashCard.dataLayer.COLUMN_TERM);
        }


        dataAccessHelper.execSQL(statement,args);

        setShuffledAnswers();
        setDataCursor();


    }

    private void setShuffledAnswers(){


        String QIDs_statement;
        String FIDs_statement;
        Cursor QIDs;
        Cursor FIDs;
        String[] args={quiz.getId().toString()};


        QIDs_statement="SELECT %s "+
                "FROM %s QP "+
                "WHERE QP.%s=? ";

        QIDs_statement =String.format(Locale.ENGLISH,QIDs_statement
                ,dataLayer.COLUMN_ID
                ,dataLayer.TABLE_NAME
                ,dataLayer.COLUMN_QUIZ_ID);

        QIDs = getTransitDataCursor(QIDs_statement,args);

        FIDs_statement="SELECT %s "+
                "FROM %s QP "+
                "WHERE QP.%s=? "+
                "ORDER BY random() ";

        FIDs_statement =String.format(Locale.ENGLISH,FIDs_statement
                ,dataLayer.COLUMN_TERM_ID
                ,dataLayer.TABLE_NAME
                ,dataLayer.COLUMN_QUIZ_ID);

        FIDs = getTransitDataCursor(FIDs_statement,args);

        getShuffledAnswers(QIDs,FIDs);

    }

    private void getShuffledAnswers(final Cursor q,final Cursor f){

        if(q != null && f != null){
            if(q.getCount() == f.getCount()){

                while (q.moveToNext() && f.moveToNext()){

                    ent_FlashCard fc = new ent_FlashCard(context);
                    fc.setId(f.getInt(f.getColumnIndex(dataLayer.COLUMN_TERM_ID)));
                    fc.get();

                    updateQuizRecord(q.getInt(q.getColumnIndex(dataLayer.COLUMN_ID))
                                    ,fc.getId()
                                    ,fc.getDefinition());
                }
                q.close();
                f.close();
            }
        }
    }

    private void updateQuizRecord(Integer id,Integer definition_id,String definition){

        try{

            ContentValues contentValues=new ContentValues();
            contentValues.put(dataLayer.COLUMN_DEFINITION_ID,definition_id);
            contentValues.put(dataLayer.COLUMN_DEFINITION,definition);

            String whereClause =String.format("%s=?",dataLayer.COLUMN_ID);
            String[] args={id.toString()};

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);
        }
        catch (Exception e){


        }
    }

    public void evaluateQuiz(){

        try{

            String statement = "UPDATE %s "+
                    "SET %s=1 "+//Quiz Answer=1
                    "WHERE %s==%s "+//term_id==definition_id
                    "AND %s=? ";//quiz_id=?
            statement=String.format(statement,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_QUIZ_ANSWER
                    ,dataLayer.COLUMN_TERM_ID
                    ,dataLayer.COLUMN_DEFINITION_ID
                    ,dataLayer.COLUMN_QUIZ_ID);

            String[] args={getQuiz().getId().toString()};
            dataAccessHelper.execSQL(statement,args);

            statement = "UPDATE %s "+
                    "SET %s=1 "+//result=1
                    "WHERE %s==%s "+//quiz_answer==user_answer
                    "AND %s=? ";//quiz_id=?

            statement=String.format(statement,dataLayer.TABLE_NAME
                    ,dataLayer.COLUMN_RESULT
                    ,dataLayer.COLUMN_QUIZ_ANSWER
                    ,dataLayer.COLUMN_USER_ANSWER
                    ,dataLayer.COLUMN_QUIZ_ID);

            dataAccessHelper.execSQL(statement,args);


        }
        catch (Exception e){

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
            values[0] = data.getString(data.getColumnIndex(dataLayer.COLUMN_TERM));
            values[1] = data.getString(data.getColumnIndex(dataLayer.COLUMN_DEFINITION));
            currentTerm_Def =values;

            return values;
        }
        else return null;

    }

    public void submitAnswer(boolean answer){

        final Integer quizID = data.getInt(data.getColumnIndex(dataLayer.COLUMN_ID));

        ContentValues contentValues = new ContentValues();
        contentValues.put(dataLayer.COLUMN_USER_ANSWER,(answer)? 1:0);

        String[] args={quizID.toString()};
        String whereClause=String.format("%s=?",dataLayer.COLUMN_ID);

        try{

            dataAccessHelper.update(dataLayer.TABLE_NAME,contentValues,whereClause,args);
            updateFlashCardStats(data.getInt(data.getColumnIndex(dataLayer.COLUMN_TERM_ID)));
        }
        catch (Exception e){


        }


    }

    private void updateFlashCardStats(final Integer id){

        final ent_FlashCard fc = new ent_FlashCard(context);
        fc.setId(id);
        fc.get();
        fc.increaseQuiz();

    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public static class dataLayer{

        public static String TABLE_NAME="QUIZ_PROCESS";

        public static String COLUMN_ID="_id";
        public static String COLUMN_QUIZ_ID="quiz_id"; // COUNT QUIZ DONE YET + 1
        public static String COLUMN_FK_DECKSET="deckset_name";
        public static String COLUMN_FK_SECTION="section_name";
        public static String COLUMN_FK_CLASS="class_name";
        public static String COLUMN_TERM_ID="term_id";
        public static String COLUMN_TERM="term";
        public static String COLUMN_DEFINITION_ID="definition_id";
        public static String COLUMN_DEFINITION="definition";
        public static String COLUMN_QUIZ_ANSWER="quiz_answer";//TRUE OR FALSE --> (TERM_ID == DEFINITION_ID)
        public static String COLUMN_USER_ANSWER="user_answer";//TRUE OR FALSE
        public static String COLUMN_RESULT="result"; //(COLUMN_QUIZ_ANSWER XOR COLUMN_USER_ANSWER) .. DEFAULT VALUE=FALSE
        public static String COLUMN_ANSWER_SPEED="answer_speed"; //IN SECONDS BETWEEN CARD LAUNCH AND USER ANSWER


    }
}

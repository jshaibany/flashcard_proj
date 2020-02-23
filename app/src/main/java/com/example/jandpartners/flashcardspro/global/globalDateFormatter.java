package com.example.jandpartners.flashcardspro.global;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class globalDateFormatter {

    private static final SimpleDateFormat isoDateTimeFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @Nullable
    public static Date convertStringToDate(@Nullable String d){

        if(d==null || d.isEmpty()) return null;

        try{

            return isoDateTimeFormat.parse(d);
        }
        catch (ParseException e){

            return null;
        }
    }

    @Nullable
    public static String formatDateToString(@Nullable Date d){

        if(d==null) return null;

        try{

            return isoDateTimeFormat.format(d);
        }
        catch (Exception e){

            return null;
        }
    }
}

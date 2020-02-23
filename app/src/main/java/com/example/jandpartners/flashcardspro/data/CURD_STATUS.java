package com.example.jandpartners.flashcardspro.data;

import java.util.HashMap;
import java.util.Map;

public enum CURD_STATUS {

    CREATED_OK("تمت عملية التسجيل"),
    CREATED_FAILED("فشلت عملية التسجيل"),
    UPDATE_OK("تمت عملية التحديث"),
    UPDATE_FAILED("فشلت عملية التحديث"),
    READ_OK("تمت عملية قراءة البيانات"),
    READ_FAILED("فشلت عملية قراءة البيانات"),
    DELETE_OK("تمت عملية الحذف"),
    DELETE_FAILED("فشلت عملية الحذف");

    private String status;

    CURD_STATUS(String s){

        this.status=s;
    }

    public String getStatus(){ return status;}

    private static final Map<CURD_STATUS,String> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(CURD_STATUS s : CURD_STATUS.values())
        {
            lookup.put(s, s.getStatus());
        }
    }

    public static String get(CURD_STATUS status){

        return lookup.get(status);
    }

}

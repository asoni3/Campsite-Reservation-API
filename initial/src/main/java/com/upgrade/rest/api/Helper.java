package com.upgrade.rest.api;

import java.time.LocalDate;
import java.util.Date;
public class Helper {

    public LocalDate parseStringToDate(String stringDate){
        LocalDate date = LocalDate.parse(stringDate);
        return date;
    }

    public boolean isValidStartDate(Date date){
        Date today = java.sql.Date.valueOf(java.time.LocalDate.now());
        if(date.equals(today)){
            return false;
        }
        return false;
    }


}

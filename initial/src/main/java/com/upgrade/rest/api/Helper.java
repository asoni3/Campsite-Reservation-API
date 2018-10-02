package com.upgrade.rest.api;

import java.time.LocalDate;

public class Helper {

    public LocalDate parseStringToDate(String stringDate){
        LocalDate date = LocalDate.parse(stringDate);
        return date;
    }


}

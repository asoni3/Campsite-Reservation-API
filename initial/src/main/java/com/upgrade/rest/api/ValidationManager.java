package com.upgrade.rest.api;

import java.time.LocalDate;
import java.util.regex.Pattern;

class ValidationManager {

    boolean validateDate(String stringDate){
        if(stringDate.length()==0){
            return false;
        }
        try{
            LocalDate date = LocalDate.parse(stringDate);

        }catch(Exception e){
            return false;
        }
        return true;
    }

    boolean validateEmailAddress(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    boolean validateFullName(String fullName){
        if(fullName.length()==0){
            return false;
        }
        String arr[] = fullName.split(" ");
        if(arr.length<2 || arr.length>3){
            return false;
        }
        return true;
    }

}

package com.upgrade.rest.api;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.Calendar;

class Helper {

    LocalDate parseStringToDate(String stringDate){
        LocalDate date = LocalDate.parse(stringDate);
        return date;
    }

    boolean isValidStartDate(Date date){

        Date today = java.sql.Date.valueOf(java.time.LocalDate.now());

        // You can reserve the campsite minimum 1 days before the date of arrival
        if(date.equals(today)){
            return false;
        }

        // You cannot make a reservation in the past
        else if(date.before(today)){
            return false;
        }

        LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
        java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

        // You can reserve a month maximum in advance
        if(date.after(lastAllowedDate)){
            return false;
        }

        return true;
    }

    boolean compareStartEndDates(Date startDate, Date endDate){

        // endDate needs to be the same or later than startDate
        if(endDate.before(startDate)){
            return false;
        }
        long days = endDate.getTime() - startDate.getTime();
        days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);


        // Campsite can be reserved for maximum 3 days
        System.out.println("Days = "+days);
        if(days > 3){

            return false;
        }

        return true;
    }

    boolean isValidEndDate(Date date){

        // You can reserve a month in advance

        LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
        java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

        // You can reserve a month maximum in advance
        if(date.after(lastAllowedDate)){
            return false;
        }

        // You cannot make a reservation in the past
        Date today = java.sql.Date.valueOf(java.time.LocalDate.now());
        return !(date.before(today));
    }

    boolean isValidDateRange(Map<String, ReservationDTO> reservationsStore, Date startDate, Date endDate,
                                    String reservationID){

        for(Map.Entry<String, ReservationDTO> entry : reservationsStore.entrySet()){
            if(reservationID.equals(entry.getKey())){
                continue;
            }
            ReservationDTO reservationDTO = entry.getValue();
            if(startDate.after(reservationDTO.getEndDate()) || endDate.before(reservationDTO.getStartDate())){
                continue;
            }
            else{
                return false;
            }
        }
        return true;
    }
}

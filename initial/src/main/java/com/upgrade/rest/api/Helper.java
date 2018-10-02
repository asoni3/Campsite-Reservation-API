package com.upgrade.rest.api;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.Calendar;

public class Helper {

    public LocalDate parseStringToDate(String stringDate){
        System.out.println("INFO: Parsing String to Date ...");
        LocalDate date = LocalDate.parse(stringDate);
        return date;
    }

    public boolean isValidStartDate(Date date){

        System.out.println("INFO: Validating Start Date ...");
        Date today = java.sql.Date.valueOf(java.time.LocalDate.now());

        // You can reserve the campsite minimum 1 days before the date of arrival
        if(date.equals(today)){
            return false;
        }
        // You cannot make a reservation in the past
        else if(date.before(today)){
            return false;
        }

//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
        java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

        // You can reserve a month maximum in advance
        if(date.after(lastAllowedDate)){
            System.out.println("Comparing "+date+ " after "+lastAllowedDate);
            return false;
        }
//        long days = ChronoUnit.DAYS.between(localDate, lastAllowedDate);

        return true;
    }

    public boolean compareStartEndDates(Date startDate, Date endDate){
        System.out.println("INFO: Validating Start and End Date Range");
        // endDate needs to be the same or later than startDate
        if(endDate.before(startDate)){
            return false;
        }
        long days = endDate.getTime() - startDate.getTime();
        days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
//        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        long days = ChronoUnit.DAYS.between(localStartDate,localEndDate);

        // Campsite can be reserved for maximum 3 days
        System.out.println("Days = "+days);
        if(days > 3){

            return false;
        }

        return true;
    }

    public boolean isValidEndDate(Date date){

        System.out.println("INFO: Validating End Date...");
        // You can reserve a month in advance

        LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);
        java.util.Date lastAllowedDate = java.sql.Date.valueOf(lastAllowedLocalDate);

        // You can reserve a month maximum in advance
        if(date.after(lastAllowedDate)){
            return false;
        }

        // You cannot make a reservation in the past
        Date today = java.sql.Date.valueOf(java.time.LocalDate.now());
        if(date.before(today)){
            return false;
        }

        return true;
    }

    public boolean isValidDateRange(Map<String, ReservationDTO> reservationsStore, Date startDate, Date endDate,
                                    String reservationID){

        System.out.println("INFO: Validating if this reservation is safe...");
        for(Map.Entry<String, ReservationDTO> entry : reservationsStore.entrySet()){
            if(reservationID.equals(entry.getKey())){
                System.out.println("Reservation is same as the person trying to modify!");
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

    public Set<Date> getAvailableDates(Map<String, ReservationDTO> reservationsStore){


        Set<Date> availableDates = new HashSet<Date>();

        LocalDate current = java.time.LocalDate.now();
        LocalDate lastAllowedLocalDate = LocalDate.now().plusMonths(1);

        for(LocalDate date = current.plusDays(1); date.isBefore(lastAllowedLocalDate) ; date.plusDays(1)){
            java.util.Date currentDate = java.sql.Date.valueOf(date);
            availableDates.add(currentDate);
        }
        java.util.Date currentDate = java.sql.Date.valueOf(lastAllowedLocalDate);
        availableDates.add(currentDate);

        for(Map.Entry<String, ReservationDTO> entry : reservationsStore.entrySet()){

            ReservationDTO reservationDTO = entry.getValue();
            Calendar start = Calendar.getInstance();
            start.setTime(reservationDTO.getStartDate());

            Calendar end = Calendar.getInstance();
            end.setTime(reservationDTO.getEndDate());
            while( !start.after(end)){
                Date targetDay = start.getTime();
                // Do Work Here
                availableDates.remove(targetDay);
                start.add(Calendar.DATE, 1);
            }

        }
        return availableDates;
    }
}

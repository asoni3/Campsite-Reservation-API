package com.upgrade.rest.api;
import java.util.Date;

class ReservationDTO {
    String fullName;
    String emailAddress;
    Date startDate;
    Date endDate;
    String reservationId;

    public ReservationDTO(){

    }
    public ReservationDTO(String fullName, String emailAddress, Date startDate, Date endDate, String reservationId){
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationId = reservationId;
    }

    public void setFullName(String name){
        fullName = name;
    }

    public void setEmailAddress(String email){
        emailAddress = email;
    }

    public void setStartDate(Date date){
        startDate = date;
    }

    public void setEndDate(Date date){
        endDate = date;
    }

    public void setReservationID(String id){
        reservationId = id;
    }

    public String getFullName(){
        return fullName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public String getReservationID(){
        return reservationId;
    }

}

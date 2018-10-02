package com.upgrade.rest.api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
@RestController
public class ReservationManager {

        // Variable Declarations
        private final AtomicLong counter = new AtomicLong();
        private final static Map<String, ReservationDTO> reservationsStore = new HashMap<String, ReservationDTO>();

        ValidationManager validationManager = new ValidationManager();
        Helper helper = new Helper();
        private static boolean dateAvailability[] = new boolean[31];

        // Home/Root Page of the service
        @RequestMapping("/")
        public MessageDTO getMessage(){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setStatus("SUCCESS");
            messageDTO.setMessage("Hello! The campsite reservation API is working! Hurray!!");
            return messageDTO;
        }

        // Get all available dates for reservation of campsite

        // In Ideal scenario, if we maintain a database, we can create an index on the table
        // and get the dates not reserved in a single query.
        // However, this is not at all how production code would be written
//        @RequestMapping("/get/dates")
//        public MessageDTO showAvailableDates(){
//
//
//        }

        // Create a new reservation for campsite
		@RequestMapping("/create/reservation")
    	public MessageDTO createReservation(@RequestParam String fullName, @RequestParam String emailAddress,
                                            @RequestParam String startDate, @RequestParam String endDate) {

            MessageDTO messageDTO = new MessageDTO();
            ReservationDTO newReservation = new ReservationDTO();
            Date startDateFormatted = null, endDateFormatted = null;
            long longCounter = counter.incrementAndGet();
            String randomUUIDString = Long.toString(longCounter);

            if(!validationManager.validateDate(startDate) || !validationManager.validateDate(endDate)){
                messageDTO.setStatus("ERROR");
                messageDTO.setMessage("Incorrect Date Format. Please follow the convention yyyy-mm-dd");
            }
            else{
                startDateFormatted = java.sql.Date.valueOf(helper.parseStringToDate(startDate));
                endDateFormatted = java.sql.Date.valueOf(helper.parseStringToDate(endDate));
                System.out.println("Start Date Formatted = "+startDateFormatted);
                System.out.println("End Date Formatted = "+endDateFormatted);
                if(!helper.isValidStartDate(startDateFormatted)){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Invalid Start Date. You can reserve minimum 1 day ahead of arrival " +
                            "and upto 1 month in advance. Please try again");
                }
                else if(!helper.isValidEndDate(endDateFormatted)){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Invalid End Date. You can reserve for maximum 3 days " +
                            "and upto 1 month in advance. Please try again");
                }
                else if(!helper.compareStartEndDates(startDateFormatted, endDateFormatted)){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Invalid Start and End Date combination. You can reserve for maximum 3 " +
                            "days and upto 1 month in advance");
                }
            }
            if(!validationManager.validateEmailAddress(emailAddress)){
                if(messageDTO.getMessage()==null){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Invalid Email Address. Please enter a valid email address");
                }
                else{
                    messageDTO.setMessage(messageDTO.getMessage() + "; Invalid Email Address. " +
                            "Please enter a valid email address");
                }
            }
            if(!validationManager.validateFullName(fullName)){
                if(messageDTO.getMessage()==null){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Please Enter First and Last Name or First, Middle and Last Name");
                }
                else{
                    messageDTO.setMessage(messageDTO.getMessage() + "; Please Enter First and Last Name or First, " +
                            "Middle and Last Name");
                }
            }
            if(!helper.isValidDateRange(reservationsStore, startDateFormatted, endDateFormatted, randomUUIDString)){

                messageDTO.setStatus("ERROR");
                messageDTO.setMessage("Unfortunately someone already has a reservation during this time frame. " +
                        "Please try to reserve some other dates. If there are no available slots please keep " +
                        "checking as new slots are available everyday. We hope to see you again! :)");

            }

            if(messageDTO.getStatus()=="ERROR"){
                return messageDTO;
            }
            else{
//                long days = endDateFormatted.getTime() - startDateFormatted.getTime();
//                days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
//                Date temp = new Date();
//                long startIndex = startDateFormatted.getTime() - temp.getTime();
//                startIndex = TimeUnit.DAYS.convert(startIndex, TimeUnit.MILLISECONDS);
//
//                for(long i=startIndex;i<days;i++){
//                    dateAvailability[(int)i] = false;
//                }
                newReservation.setEmailAddress(emailAddress);
                newReservation.setFullName(fullName);
                newReservation.setStartDate(startDateFormatted);
                newReservation.setEndDate(endDateFormatted);
                newReservation.setReservationID(randomUUIDString);

                // Storing the new reservation
                reservationsStore.put(randomUUIDString, newReservation);

                messageDTO.setStatus("SUCCESS");
                messageDTO.setMessage("Congratulations!! You have successfully reserved the Campsite! Please note" +
                        " down your booking number: "+randomUUIDString);
            }
			return messageDTO;
		}

		// Modify an existing reservation
		@RequestMapping("/modify/reservation")
        public MessageDTO modifyReservation(@RequestParam String reservationID,
                                            @RequestParam(value="fullName", defaultValue="") String fullName,
                                            @RequestParam(value="emailAddress", defaultValue="") String emailAddress,
                                            @RequestParam String startDate,
                                            @RequestParam String endDate){

            MessageDTO messageDTO = new MessageDTO();
            ReservationDTO currentReservation;
            Date startDateFormatted = null, endDateFormatted = null;
            if(reservationsStore.containsKey(reservationID)){

                currentReservation = reservationsStore.get(reservationID);

                if(!validationManager.validateDate(startDate) || !validationManager.validateDate(endDate)){
                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Incorrect Date Format. Please follow the convention yyyy-mm-dd");
                }
                else{
                    startDateFormatted = java.sql.Date.valueOf(helper.parseStringToDate(startDate));
                    endDateFormatted = java.sql.Date.valueOf(helper.parseStringToDate(endDate));
                    if(!helper.isValidStartDate(startDateFormatted)){
                        messageDTO.setStatus("ERROR");
                        messageDTO.setMessage("Invalid Start Date. You can reserve minimum 1 day ahead of arrival " +
                                "and upto 1 month in advance. Please try again");
                    }
                    else if(!helper.isValidEndDate(endDateFormatted)){
                        messageDTO.setStatus("ERROR");
                        messageDTO.setMessage("Invalid End Date. You can reserve for maximum 3 days " +
                                "and upto 1 month in advance. Please try again");
                    }
                    else if(!helper.compareStartEndDates(startDateFormatted, endDateFormatted)){
                        messageDTO.setStatus("ERROR");
                        messageDTO.setMessage("Invalid Start and End Date combination. You can reserve for maximum 3 " +
                                "days and upto 1 month in advance");
                    }
                }


                if(!helper.isValidDateRange(reservationsStore, startDateFormatted, endDateFormatted, reservationID)){

                    messageDTO.setStatus("ERROR");
                    messageDTO.setMessage("Unfortunately someone already has a reservation during this time frame. " +
                            "Please try to reserve some other dates. If there are no available slots please keep " +
                            "checking as new slots are available everyday. We hope to see you again! :)");

                }
                if(!validationManager.validateFullName(fullName)){
                    if(messageDTO.getMessage()==null){
                        messageDTO.setStatus("ERROR");
                        messageDTO.setMessage("Please Enter First and Last Name or First, Middle and Last Name");
                    }
                    else{
                        messageDTO.setMessage(messageDTO.getMessage() + "; Please Enter First " +
                                "and Last Name or First, " +
                                "Middle and Last Name");
                    }
                }

                if(!validationManager.validateEmailAddress(emailAddress)){
                    if(messageDTO.getMessage()==null){
                        messageDTO.setStatus("ERROR");
                        messageDTO.setMessage("Invalid Email Address. Please enter a valid email address");
                    }
                    else{
                        messageDTO.setMessage(messageDTO.getMessage() + "; Invalid Email Address. " +
                                "Please enter a valid email address");
                    }
                }

                if(messageDTO.getStatus()=="ERROR"){
                    return messageDTO;
                }

                else {
                    // If everything is valid, change the reservation
                    currentReservation.setEndDate(endDateFormatted);
                    currentReservation.setStartDate(startDateFormatted);
                    if(emailAddress.length()!=0){
                        currentReservation.setEmailAddress(emailAddress);
                    }
                    if(fullName.length()!=0){
                        currentReservation.setFullName(fullName);
                    }

                    // Storing the modified reservation
                    reservationsStore.put(reservationID, currentReservation);
                    messageDTO.setStatus("SUCCESS");
                    messageDTO.setMessage("Hurray! Your reservation "+reservationID+" is modified!");
                }

            }
            else{
                messageDTO.setStatus("ERROR");
                messageDTO.setMessage(" Oops! We were not able to find the reservation you are looking for. " +
                        "Please re-enter the correct reservation ID!");
            }
            return messageDTO;
        }

        // Delete an existing reservation
        @RequestMapping("/delete/reservation")
        public MessageDTO deleteReservation(@RequestParam String reservationID){
            MessageDTO messageDTO = new MessageDTO();
            if(reservationsStore.containsKey(reservationID)){
                reservationsStore.remove(reservationID);
                messageDTO.setStatus("SUCCESS");
                messageDTO.setMessage("Your reservation (ID: "+reservationID+") has been deleted :( ." +
                        " Hope to see you some other day at the campsite! :) :D ");
            }
            else{
                messageDTO.setStatus("ERROR");
                messageDTO.setMessage(" Oops! We were not able to find the reservation you are looking for. " +
                        "Please re-enter the correct reservation ID!");
            }
            return messageDTO;
        }

        // Get information for your reservation
        @RequestMapping("get/reservation")
        public ReservationDTO getReservationInfo(@RequestParam String reservationID){

            return reservationsStore.get(reservationID);
        }
}

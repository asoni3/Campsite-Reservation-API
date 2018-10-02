package com.upgrade.rest.api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
@RestController
public class ReservationManager {

        // Variable Declarations
        private final AtomicLong counter = new AtomicLong();
        private final static Map<String, ReservationDTO> reservationsStore = new HashMap<String, ReservationDTO>();
        private final static Calendar calendarInstance = Calendar.getInstance();
        ValidationManager validationManager = new ValidationManager();
        Helper helper = new Helper();

        // Home/Root Page of the service
        @RequestMapping("/")
        public MessageDTO getMessage(){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setStatus("SUCCESS");
            messageDTO.setMessage("Hello! The campsite reservation API is working! Hurray!!");
            return messageDTO;
        }

        // Create a new reservation for campsite
		@RequestMapping("/create/reservation")
    	public MessageDTO createReservation(@RequestParam String fullName, @RequestParam String emailAddress,
                                            @RequestParam String startDate, @RequestParam String endDate) {

            MessageDTO messageDTO = new MessageDTO();
            ReservationDTO newReservation = new ReservationDTO();
            if(!validationManager.validateDate(startDate) || !validationManager.validateDate(endDate)){
                messageDTO.setStatus("ERROR");
                messageDTO.setMessage("Incorrect Date Format. Please follow the convention yyyy-mm-dd");
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
            if(messageDTO.getStatus()=="ERROR"){
                return messageDTO;
            }
            else{
                long longCounter = counter.incrementAndGet();
                String randomUUIDString = Long.toString(longCounter);
                newReservation.setEmailAddress(emailAddress);
                newReservation.setFullName(fullName);
                newReservation.setStartDate(java.sql.Date.valueOf(helper.parseStringToDate(startDate)));
                newReservation.setEndDate(java.sql.Date.valueOf(helper.parseStringToDate(endDate)));
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
            if(reservationsStore.containsKey(reservationID)){
                ReservationDTO currentReservation = reservationsStore.get(reservationID);

                if(fullName.length()!=0){
                    currentReservation.setFullName(fullName);
                }
                if(emailAddress.length()!=0){
                    currentReservation.setEmailAddress(emailAddress);
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
}

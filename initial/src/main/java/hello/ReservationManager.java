package hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

@RestController
public class ReservationManager {

        @RequestMapping("/message")
        public MessageDTO getMessage(){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("Hello! The campsite reservation API is working! Hurray!!");
            return messageDTO;
        }

		@RequestMapping("/reserve")
    	public ReservationDTO reserveCampsite(@RequestParam String fullName, @RequestParam String emailAddress, @RequestParam Date startDate, @RequestParam Date endDate) {

			ReservationDTO newReservation = new ReservationDTO();

			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();

			newReservation.setEmailAddress(emailAddress);
			newReservation.setFullName(fullName);
			newReservation.setStartDate(startDate);
			newReservation.setEndDate(endDate);
			newReservation.setReservationID(randomUUIDString);

			return newReservation;
		}
}

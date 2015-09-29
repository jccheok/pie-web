package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Event;
import pie.utilities.DatabaseConnector;

public class EventService {

	public Event getEvent(int eventID) {
		
		Event event = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Event` WHERE eventID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, eventID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				event = new Event();
				event.setEventID(eventID);
				event.setEventAuthor(new StaffService().getStaff(resultSet.getInt("teacherID")));
				event.setEventTitle(resultSet.getString("eventTitle"));
				event.setAddress(new AddressService().getAddress(resultSet.getInt("addressID")));
				event.setEventDescription(resultSet.getString("eventDescription"));
				event.setEventAttire(resultSet.getString("eventAttire"));
				event.setEventStartDate(new Date(resultSet.getTimestamp("eventStartDate").getTime()));
				event.setEventEndDate(new Date(resultSet.getTimestamp("eventEndDate").getTime()));
				event.setEventDateCreated(new Date(resultSet.getTimestamp("eventDateCreated").getTime()));
				event.setEventIsDraft(resultSet.getInt("eventIsDraft") == 1);
				event.setEventIsTemplate(resultSet.getInt("eventIsTemplate") == 1);
				event.setEventIsDeleted(resultSet.getInt("eventIsDeleted") == 1);
				event.setEventDateDeleted(new Date(resultSet.getTimestamp("eventDateDeleted").getTime()));
				event.setResponseQuestion(new ResponseQuestionService().getResponseQuestion(resultSet.getInt("responseQuestionID")));
				event.setEventIsRequired(resultSet.getInt("eventIsRequired") == 1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return event;
	}
}

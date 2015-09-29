package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Address;
import pie.Event;
import pie.Group;
import pie.ResponseQuestion;
import pie.Staff;
import pie.User;
import pie.util.DatabaseConnector;

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
				event.setEventAuthor(new StaffService().getStaff(resultSet
						.getInt("staffID")));
				event.setEventTitle(resultSet.getString("eventTitle"));
				event.setAddress(new AddressService().getAddress(resultSet
						.getInt("addressID")));
				event.setEventDescription(resultSet
						.getString("eventDescription"));
				event.setEventAttire(resultSet.getString("eventAttire"));
				event.setEventStartDate(new Date(resultSet.getTimestamp(
						"eventStartDate").getTime()));
				event.setEventEndDate(new Date(resultSet.getTimestamp(
						"eventEndDate").getTime()));
				event.setEventDateCreated(new Date(resultSet.getTimestamp(
						"eventDateCreated").getTime()));
				event.setEventIsDraft(resultSet.getInt("eventIsDraft") == 1);
				event.setEventIsTemplate(resultSet.getInt("eventIsTemplate") == 1);
				event.setEventIsDeleted(resultSet.getInt("eventIsDeleted") == 1);
				event.setEventDateDeleted(new Date(resultSet.getTimestamp(
						"eventDateDeleted").getTime()));
				event.setResponseQuestion(new ResponseQuestionService()
						.getResponseQuestion(resultSet
								.getInt("responseQuestionID")));
				event.setEventIsRequired(resultSet.getInt("eventIsRequired") == 1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return event;
	}

	public Group[] getGroupRecipient(int eventID) {
		Group[] groupRecipient = {};

		// Write codes to retrieve Group Recipients of Event

		return groupRecipient;
	}

	public User[] getUserRecipient(int eventID) {
		User[] userRecipient = {};

		// Write codes to retrieve User Recipients of Event

		return userRecipient;
	}

	public int createEvent(String eventTitle, Staff staff, Address address,
			String eventDescription, String eventAttire, Date eventStartDate,
			Date eventEndDate, ResponseQuestion responseQuestion) {
		int eventID = -1;

		// Write codes to create Event

		return eventID;
	}

	public int createEventAsDraft(String eventTitle, Staff staff,
			Address address, String eventDescription, String eventAttire,
			Date eventStartDate, Date eventEndDate,
			ResponseQuestion responseQuestion) {
		int eventID = -1;
		int eventIsDraft = 1;

		// Write codes to create Event as Draft

		return eventID;
	}

	public int createEventAsTemplate(String eventTitle, Staff staff,
			Address address, String eventDescription, String eventAttire,
			Date eventStartDate, Date eventEndDate,
			ResponseQuestion responseQuestion) {
		int eventID = -1;
		int eventIsTemplate = 1;

		// Write codes to create Event as Template

		return eventID;
	}

	public boolean deleteEvent(int eventID) {
		boolean deleteResult = false;

		// Write codes to delete Event

		return deleteResult;
	}

	public boolean sendEventDraft(int eventID) {
		boolean sendResult = false;

		// Write codes to send Event

		return sendResult;
	}

	public Event[] getEventsIsDraft(Staff staff) {
		Event[] events = {};

		// Write codes to retrieve all the Events that are saved as Drafts by
		// Staff

		return events;
	}

	public Event[] getEventsIsTemplate(Staff staff) {
		Event[] events = {};

		// Write codes to retrieve all Events that are saved as Template by
		// Staff

		return events;
	}
}

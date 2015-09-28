package pie;

import java.util.Date;

public class Event {

	private int eventID;
	private Staff eventAuthor;
	private Address address;
	private String eventTitle;
	private String eventDescription;
	private String eventAttire;
	private Date eventStartDate;
	private Date eventEndDate;
	private Date eventDateCreated;
	private boolean eventIsDraft;
	private boolean eventIsTemplate;
	private boolean eventIsDeleted;
	private Date eventDateDeleted;
	private ResponseQuestion responseQuestion;
	private boolean eventIsRequired;
	
	public int getEventID() {
		return eventID;
	}
	
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String getEventTitle() {
		return eventTitle;
	}
	
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	public String getEventDescription() {
		return eventDescription;
	}
	
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	public String getEventAttire() {
		return eventAttire;
	}
	
	public void setEventAttire(String eventAttire) {
		this.eventAttire = eventAttire;
	}
	
	public Date getEventStartDate() {
		return eventStartDate;
	}
	
	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	
	public Date getEventEndDate() {
		return eventEndDate;
	}
	
	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	
	public Date getEventDateCreated() {
		return eventDateCreated;
	}
	
	public void setEventDateCreated(Date eventDateCreated) {
		this.eventDateCreated = eventDateCreated;
	}
	
	public boolean isEventIsDraft() {
		return eventIsDraft;
	}
	
	public void setEventIsDraft(boolean eventIsDraft) {
		this.eventIsDraft = eventIsDraft;
	}
	
	public boolean isEventIsTemplate() {
		return eventIsTemplate;
	}
	
	public void setEventIsTemplate(boolean eventIsTemplate) {
		this.eventIsTemplate = eventIsTemplate;
	}
	
	public boolean isEventIsDeleted() {
		return eventIsDeleted;
	}
	
	public void setEventIsDeleted(boolean eventIsDeleted) {
		this.eventIsDeleted = eventIsDeleted;
	}
	
	public Date getEventDateDeleted() {
		return eventDateDeleted;
	}
	
	public void setEventDateDeleted(Date eventDateDeleted) {
		this.eventDateDeleted = eventDateDeleted;
	}
	
	public ResponseQuestion getResponseQuestion() {
		return responseQuestion;
	}
	
	public void setResponseQuestion(ResponseQuestion responseQuestion) {
		this.responseQuestion = responseQuestion;
	}
	
	public boolean isEventIsRequired() {
		return eventIsRequired;
	}
	
	public void setEventIsRequired(boolean eventIsRequired) {
		this.eventIsRequired = eventIsRequired;
	}

	public Staff getEventAuthor() {
		return eventAuthor;
	}

	public void setEventAuthor(Staff eventAuthor) {
		this.eventAuthor = eventAuthor;
	}
	
	
	
}

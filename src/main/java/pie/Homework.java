package pie;

import java.util.Date;

public class Homework {

	private int homeworkID;
	private Staff homeworkAuthor;
	private String homeworkTitle;
	private String homeworkSubject;
	private String homeworkDescription;
	private int homeworkMinutesRequired;
	private Date homeworkDueDate;
	private boolean homeworkIsOpen;
	private Date homeworkDateCreated;
	private boolean homeworkIsDraft;
	private boolean homeworkIsTemplate;
	private boolean homeworkIsDeleted;
	private Date homeworkDateDeleted;

	
	
	public Homework(int homeworkID, Staff homeworkAuthor, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate, boolean homeworkOpen,
			Date homeworkDateCreated, boolean homeworkIsDraft, boolean homeworkIsTemplate, boolean homeworkIsDeleted,
			Date homeworkDateDeleted) {
		setHomeworkID(homeworkID);
		setHomeworkAuthor(homeworkAuthor);
		setHomeworkTitle(homeworkTitle);
		setHomeworkSubject(homeworkSubject);
		setHomeworkDescription(homeworkDescription);
		setHomeworkMinutesRequired(homeworkMinutesRequired);
		setHomeworkDueDate(homeworkDueDate);
		setHomeworkIsOpen(homeworkIsDeleted);
		setHomeworkDateCreated(homeworkDateCreated);
		setHomeworkIsDraft(homeworkIsDraft);
		setHomeworkIsTemplate(homeworkIsTemplate);
		setHomeworkIsDeleted(homeworkIsDeleted);
		setHomeworkDateDeleted(homeworkDateDeleted);
	}

	public int getHomeworkID() {
		return homeworkID;
	}
	
	public void setHomeworkID(int homeworkID) {
		this.homeworkID = homeworkID;
	}
	
	public String getHomeworkTitle() {
		return homeworkTitle;
	}
	
	public void setHomeworkTitle(String homeworkTitle) {
		this.homeworkTitle = homeworkTitle;
	}
	
	public String getHomeworkSubject() {
		return homeworkSubject;
	}
	
	public void setHomeworkSubject(String homeworkSubject) {
		this.homeworkSubject = homeworkSubject;
	}
	
	public String getHomeworkDescription() {
		return homeworkDescription;
	}
	
	public void setHomeworkDescription(String homeworkDescription) {
		this.homeworkDescription = homeworkDescription;
	}
	
	public int getHomeworkMinutesRequired() {
		return homeworkMinutesRequired;
	}
	
	public void setHomeworkMinutesRequired(int homeworkMinutesRequired) {
		this.homeworkMinutesRequired = homeworkMinutesRequired;
	}
	
	public Date getHomeworkDueDate() {
		return homeworkDueDate;
	}
	
	public void setHomeworkDueDate(Date homeworkDueDate) {
		this.homeworkDueDate = homeworkDueDate;
	}
	
	public boolean isHomeworkOpen() {
		return homeworkIsOpen;
	}
	
	public void setHomeworkIsOpen(boolean homeworkIsOpen) {
		this.homeworkIsOpen = homeworkIsOpen;
	}
	
	public Date getHomeworkDateCreated() {
		return homeworkDateCreated;
	}
	
	public void setHomeworkDateCreated(Date homeworkDateCreated) {
		this.homeworkDateCreated = homeworkDateCreated;
	}
	
	public boolean isHomeworkIsDraft() {
		return homeworkIsDraft;
	}
	
	public void setHomeworkIsDraft(boolean homeworkIsDraft) {
		this.homeworkIsDraft = homeworkIsDraft;
	}
	
	public boolean isHomeworkIsTemplate() {
		return homeworkIsTemplate;
	}
	
	public void setHomeworkIsTemplate(boolean homeworkIsTemplate) {
		this.homeworkIsTemplate = homeworkIsTemplate;
	}
	
	public boolean isHomeworkIsDeleted() {
		return homeworkIsDeleted;
	}
	
	public void setHomeworkIsDeleted(boolean homeworkIsDeleted) {
		this.homeworkIsDeleted = homeworkIsDeleted;
	}
	
	public Date getHomeworkDateDeleted() {
		return homeworkDateDeleted;
	}
	
	public void setHomeworkDateDeleted(Date homeworkDateDeleted) {
		this.homeworkDateDeleted = homeworkDateDeleted;
	}

	public Staff getHomeworkAuthor() {
		return homeworkAuthor;
	}

	public void setHomeworkAuthor(Staff homeworkAuthor) {
		this.homeworkAuthor = homeworkAuthor;
	}
	
}

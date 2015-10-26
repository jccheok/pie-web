package pie;

import java.util.Date;

public class Homework {

	private int homeworkID;
	private Staff homeworkAuthor;
	private String homeworkTitle;
	private String homeworkSubject;
	private String homeworkDescription;
	private int homeworkMinutesReqStudent;
	private Date homeworkDateCreated;
	private boolean homeworkIsDraft;
	private boolean homeworkIsDeleted;
	private String homeworkLevel;

	public Homework(int homeworkID, Staff homeworkAuthor, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesReqStudent, Date homeworkDateCreated,
			boolean homeworkIsDraft, boolean homeworkIsDeleted, String homeworkLevel) {
		setHomeworkID(homeworkID);
		setHomeworkAuthor(homeworkAuthor);
		setHomeworkTitle(homeworkTitle);
		setHomeworkSubject(homeworkSubject);
		setHomeworkDescription(homeworkDescription);
		sethomeworkMinutesReqStudent(homeworkMinutesReqStudent);
		setHomeworkDateCreated(homeworkDateCreated);
		setHomeworkIsDraft(homeworkIsDraft);
		setHomeworkIsDeleted(homeworkIsDeleted);
		setHomeworkLevel(homeworkLevel);
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

	public int gethomeworkMinutesReqStudent() {
		return homeworkMinutesReqStudent;
	}

	public void sethomeworkMinutesReqStudent(int homeworkMinutesReqStudent) {
		this.homeworkMinutesReqStudent = homeworkMinutesReqStudent;
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

	public boolean isHomeworkIsDeleted() {
		return homeworkIsDeleted;
	}

	public void setHomeworkIsDeleted(boolean homeworkIsDeleted) {
		this.homeworkIsDeleted = homeworkIsDeleted;
	}

	public Staff getHomeworkAuthor() {
		return homeworkAuthor;
	}

	public void setHomeworkAuthor(Staff homeworkAuthor) {
		this.homeworkAuthor = homeworkAuthor;
	}

	public String getHomeworkLevel() {
		return homeworkLevel;
	}

	public void setHomeworkLevel(String homeworkLevel) {
		this.homeworkLevel = homeworkLevel;
	}

}

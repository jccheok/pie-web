package pie;

import java.util.Date;

public class UserHomework {
	private int userHomeworkID;
	private int homeworkID;
	private int userID;
	private boolean isRead;
	private boolean isSubmitted;
	private Date submissionDate;
	private boolean isArchived;
	private Date dateArchived;
	private Date dateRead;
	private char grade;
	private boolean isMarked;

	public UserHomework(int userHomeworkID, int homeworkID, int userID, boolean isRead, boolean isSubmitted,
			Date submissionDate, boolean isArchived, Date dateArchived, Date dateRead, char grade, boolean isMarked) {
		// TODO Auto-generated constructor stub

		setUserHomeworkID(userHomeworkID);
		setHomeworkID(homeworkID);
		setUserID(userID);
		setRead(isRead);
		setSubmitted(isSubmitted);
		setSubmissionDate(submissionDate);
		setArchived(isArchived);
		setDateArchived(dateArchived);
		setDateRead(dateRead);
		setGrade(grade);
		setMarked(isMarked);
	}

	public int getUserHomeworkID() {
		return userHomeworkID;
	}

	public void setUserHomeworkID(int userHomeworkID) {
		this.userHomeworkID = userHomeworkID;
	}

	public int getHomeworkID() {
		return homeworkID;
	}

	public void setHomeworkID(int homeworkID) {
		this.homeworkID = homeworkID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public Date getDateRead() {
		return dateRead;
	}

	public void setDateRead(Date dateRead) {
		this.dateRead = dateRead;
	}

	public char getGrade() {
		return grade;
	}

	public void setGrade(char grade) {
		this.grade = grade;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

}

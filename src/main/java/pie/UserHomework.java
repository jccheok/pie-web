package pie;

import java.util.Date;

public class UserHomework {
	private int userHomeworkID;
	private Homework homework;
	private User user;
	private boolean isRead;
	private boolean isSubmitted;
	private Date submissionDate;
	private boolean isArchived;
	private Date dateArchived;
	private Date dateRead;
	private String grade;
	private boolean isMarked;
	private boolean isDeleted;
	private boolean isAcknowledged;
	
	public UserHomework(int userHomeworkID, Homework homework, User user, boolean isRead, boolean isSubmitted,
			Date submissionDate, boolean isArchived, Date dateArchived, Date dateRead, String grade, boolean isMarked, boolean isDeleted, boolean isAcknowledged) {
		// TODO Auto-generated constructor stub

		setUserHomeworkID(userHomeworkID);
		setHomework(homework);	
		setUser(user);		
		setRead(isRead);
		setSubmitted(isSubmitted);
		setSubmissionDate(submissionDate);
		setArchived(isArchived);
		setDateArchived(dateArchived);
		setDateRead(dateRead);
		setGrade(grade);
		setMarked(isMarked);
		setDeleted(isDeleted);
		setAcknowledged(isAcknowledged);
	}

	public int getUserHomeworkID() {
		return userHomeworkID;
	}

	public void setUserHomeworkID(int userHomeworkID) {
		this.userHomeworkID = userHomeworkID;
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

	public Homework getHomework() {
		return homework;
	}

	public void setHomework(Homework homework) {
		this.homework = homework;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isAcknowledged() {
		return isAcknowledged;
	}

	public void setAcknowledged(boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

}

package pie;

import java.util.Date;

public class Note {

	private int noteID;
	private Staff staff;
	private String title;
	private String description;
	private boolean isDraft;
	private boolean isDeleted;
	private Date dateCreated;
	private Date dateDeleted;
	private ResponseQuestion responseQuestion;
	
	public Note(int noteID, Staff staff, String title, String description,
			boolean isDraft, boolean isDeleted, Date dateCreated,
			Date dateDeleted, ResponseQuestion responseQuestion) {
		
		setNoteID(noteID);
		setStaff(staff);
		setTitle(title);
		setDescription(description);
		setDraft(isDraft);
		setDeleted(isDeleted);
		setDateCreated(dateCreated);
		setDateDeleted(dateDeleted);
		setResponseQuestion(responseQuestion);
	}

	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDraft() {
		return isDraft;
	}

	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateDeleted() {
		return dateDeleted;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	public ResponseQuestion getResponseQuestion() {
		return responseQuestion;
	}

	public void setResponseQuestion(ResponseQuestion responseQuestion) {
		this.responseQuestion = responseQuestion;
	}
	
}

package pie;

import java.util.Date;

public class Note {

	private int noteID;
	private Staff noteAuthor;
	private String noteTitle;
	private String noteDescription;
	private boolean noteIsTemplate;
	private boolean noteIsDraft;
	private boolean noteIsDeleted;
	private Date noteDateCreated;
	private Date noteDateDeleted;
	private ResponseQuestion noteQuestionID;

	public Note(int noteID, Staff noteAuthor, String noteTitle,
			String noteDescription, boolean noteIsTemplate,
			boolean noteIsDraft, boolean noteIsDeleted, Date noteDateCreated,
			Date noteDateDeleted, ResponseQuestion noteQuestionID) {
		setNoteID(noteID);
		setNoteAuthor(noteAuthor);
		setNoteTitle(noteTitle);
		setNoteDescription(noteDescription);
		setNoteIsTemplate(noteIsTemplate);
		setNoteIsDraft(noteIsDraft);
		setNoteIsDeleted(noteIsDeleted);
		setNoteDateCreated(noteDateCreated);
		setNoteDateDeleted(noteDateDeleted);
		setNoteQuestionID(noteQuestionID);
	}

	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteDescription() {
		return noteDescription;
	}

	public void setNoteDescription(String noteDescription) {
		this.noteDescription = noteDescription;
	}

	public boolean isNoteIsTemplate() {
		return noteIsTemplate;
	}

	public void setNoteIsTemplate(boolean noteIsTemplate) {
		this.noteIsTemplate = noteIsTemplate;
	}

	public boolean isNoteIsDraft() {
		return noteIsDraft;
	}

	public void setNoteIsDraft(boolean noteIsDraft) {
		this.noteIsDraft = noteIsDraft;
	}

	public boolean isNoteIsDeleted() {
		return noteIsDeleted;
	}

	public void setNoteIsDeleted(boolean noteIsDeleted) {
		this.noteIsDeleted = noteIsDeleted;
	}

	public Date getNoteDateCreated() {
		return noteDateCreated;
	}

	public void setNoteDateCreated(Date noteDateCreated) {
		this.noteDateCreated = noteDateCreated;
	}

	public Date getNoteDateDeleted() {
		return noteDateDeleted;
	}

	public void setNoteDateDeleted(Date noteDateDeleted) {
		this.noteDateDeleted = noteDateDeleted;
	}

	public Staff getNoteAuthor() {
		return noteAuthor;
	}

	public void setNoteAuthor(Staff noteAuthor) {
		this.noteAuthor = noteAuthor;
	}
	
	public ResponseQuestion getNoteQuestionID() {
		return noteQuestionID;
	}
	
	public void setNoteQuestionID(ResponseQuestion noteQuestionID) {
		this.noteQuestionID = noteQuestionID;
	}

}

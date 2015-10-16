package pie;

import java.util.Date;

public class UserNote {

	private int userNoteID;
	private ResponseOption repsonseOption;
	private	Note note;
	private User user;
	private boolean isRead;
	private boolean isArchive;
	private Date dateRead;
	private Date dateArchived;
	private String responseText;
	
	
	public UserNote(int userNoteID, ResponseOption repsonseOption, Note note, User user, boolean isRead,
			boolean isArchive, Date dateRead, Date dateArchived, String responseText) {
		
		setUserNoteID(userNoteID);
		setNote(note);
		setUser(user);
		setRepsonseOption(repsonseOption);
		setResponseText(responseText);
		setArchive(isArchive);
		setRead(isRead);
		setDateArchived(dateArchived);
		setDateRead(dateRead);
		
	}
	
	
	public int getUserNoteID() {
		return userNoteID;
	}
	public void setUserNoteID(int userNoteID) {
		this.userNoteID = userNoteID;
	}
	public ResponseOption getRepsonseOption() {
		return repsonseOption;
	}
	public void setRepsonseOption(ResponseOption repsonseOption) {
		this.repsonseOption = repsonseOption;
	}
	public Note getNote() {
		return note;
	}
	public void setNote(Note note) {
		this.note = note;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public boolean isArchive() {
		return isArchive;
	}
	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}
	public Date getDateRead() {
		return dateRead;
	}
	public void setDateRead(Date dateRead) {
		this.dateRead = dateRead;
	}
	public Date getDateArchived() {
		return dateArchived;
	}
	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	
	
	
}

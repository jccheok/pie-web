package pie;

public class NoteAttachment {

	private int noteAttachmentID;
	private String attachmentURL;
	private Note note;
	
	public NoteAttachment(int noteAttachmentID, String attachmentURL, Note note) {
		setNoteAttachmentID(noteAttachmentID);
		setAttachmentURL(attachmentURL);
		setNote(note);
	}

	public int getNoteAttachmentID() {
		return noteAttachmentID;
	}

	public void setNoteAttachmentID(int noteAttachmentID) {
		this.noteAttachmentID = noteAttachmentID;
	}

	public String getAttachmentURL() {
		return attachmentURL;
	}

	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

}

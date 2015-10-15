package pie;

public class NoteAttachment {

	private int noteAttachmentID;
	private String attachmentURL;
	private int noteID;
	
	public NoteAttachment(int noteAttachmentID, String attachmentURL, int noteID) {
		setNoteAttachmentID(noteAttachmentID);
		setAttachmentURL(attachmentURL);
		setNoteID(noteID);
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

	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

}

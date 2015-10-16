package pie;

public class NoteAttachment {

	private int noteAttachmentID;
	private String attachmentURL;
	private Note noteID;
	
	public NoteAttachment(int noteAttachmentID, String attachmentURL, Note noteID) {
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

	public Note getNoteID() {
		return noteID;
	}

	public void setNoteID(Note noteID) {
		this.noteID = noteID;
	}

}

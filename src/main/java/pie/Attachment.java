package pie;

public class Attachment {
	
	private int attachmentID;
	private String attachmentURL;
	private AttachmentType attachmentType;
	
	public Attachment(int attachmentID, String attachmentURL, AttachmentType attachmentType) {
		setAttachmentID(attachmentID);
		setAttachmentURL(attachmentURL);
		setAttachmentType(attachmentType);
	}

	public int getAttachmentID() {
		return attachmentID;
	}
	
	public void setAttachmentID(int attachmentID) {
		this.attachmentID = attachmentID;
	}
	
	public String getAttachmentURL() {
		return attachmentURL;
	}
	
	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}
	
	public AttachmentType getAttachmentType() {
		return attachmentType;
	}
	
	public void setAttachmentType(AttachmentType attachmentType) {
		this.attachmentType = attachmentType;
	}

	
	
}

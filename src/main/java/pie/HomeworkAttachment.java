package pie;

public class HomeworkAttachment {

	private int homeworkAttachmentID;
	private String attachmentURL;
	private int homeworkID;
	
	public HomeworkAttachment(int homeworkAttachmentID, String attachmentURL, int homeworkID) {
		setHomeworkAttachmentID(homeworkAttachmentID);
		setAttachmentURL(attachmentURL);
		setHomeworkID(homeworkID);
	}
	
	public int getHomeworkAttachmentID() {
		return homeworkAttachmentID;
	}
	public void setHomeworkAttachmentID(int homeworkAttachmentID) {
		this.homeworkAttachmentID = homeworkAttachmentID;
	}
	public String getAttachmentURL() {
		return attachmentURL;
	}
	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}
	public int getHomeworkID() {
		return homeworkID;
	}
	public void setHomeworkID(int homeworkID) {
		this.homeworkID = homeworkID;
	}
		
}

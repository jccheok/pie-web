package pie;

public class HomeworkAttachment {

	private int homeworkAttachmentID;
	private String attachmentURL;
	private Homework homework;
	
	public HomeworkAttachment(int homeworkAttachmentID, String attachmentURL, Homework homework) {
		setHomeworkAttachmentID(homeworkAttachmentID);
		setAttachmentURL(attachmentURL);
		setHomework(homework);
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
	
	public Homework getHomework() {
		return homework;
	}
	
	public void setHomework(Homework homework) {
		this.homework = homework;
	}
		
}

package pie.constants;

public enum PublishHomeworkResult {

	SUCCESS("Homework successfully published!"), 
	FAILED_TO_UPDATE_GROUP("Failed to send homework to group"), 
	FAILED_TO_SEND_TO_MEMBERS("The homework failed to send to members"),
	FAILED_DRAFT("Failed to set the homework to not draft");

	private String defaultMessage;
	
	PublishHomeworkResult(String defaultMessage){
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	
}

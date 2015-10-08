package pie.constants;

public enum PublishNoteResult {
	
	SUCCESS("Note successfully published!"), 
	FAILED_TO_UPDATE_GROUP("Failed to send note to group"), 
	FAILED_TO_SEND_TO_MEMBERS("The note failed to send to members"),
	FAILED_DRAFT("Failed to set the note to not draft");

	private String defaultMessage;
	
	PublishNoteResult(String defaultMessage){
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	
}

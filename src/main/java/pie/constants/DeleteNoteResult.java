package pie.constants;

public enum DeleteNoteResult {
	SUCCESS("Successfully deleted note."), 
	NOTE_DOES_NOT_EXIST("The note you are attempting to delete does not exist"),
	FAILED_TO_SET_TO_DELETE("Failed to set the note to isDeleted"),
	FAILED_REMOVE_NOTE("Failed to remove draft note from database");
	
	private String defaultMessage;

	DeleteNoteResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

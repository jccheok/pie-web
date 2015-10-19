package pie.constants;

public enum DeleteHomeworkResult {

	SUCCESS("Successfully deleted homework"),
	HOMEWORK_DOES_NOT_EXIST("The homework you attempted to delete does not exist"),
	FAILED_TO_SET_TO_DELETE("Failed to set the homework to isDeleted");

	private String defaultMessage;

	DeleteHomeworkResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

}

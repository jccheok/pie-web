package pie.constants;

public enum DeleteHomeworkResult {

	SUCCESS("Successfully deleted homework"),
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

package pie.constants;

public enum PublishHomeworkResult {

	SUCCESS("Homework successfully published!"), FAILED_TO_UPDATE_HOMEWORK("Failed to publish homework"), FAILED_DRAFT(
			"Failed to set the homework to not draft");

	private String defaultMessage;

	PublishHomeworkResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

}

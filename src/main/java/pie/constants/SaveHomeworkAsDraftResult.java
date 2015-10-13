package pie.constants;

public enum SaveHomeworkAsDraftResult {
	SUCCESS("Homework successfully saved"), FAILED_TO_SAVE_HOMEWORK_AS_DRAFT("Failed to save homework as draft");

	private String defaultMessage;

	SaveHomeworkAsDraftResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

}

package pie.constants;

public enum UpdateHomeworkDraftResult {

	SUCCESS("Homework successfully updated."), 
	FAIL_TO_UPDATE_HOMEWORK("Fail to update homework"), 
	HOMEWORK_IS_NOT_DRAFT("Homework you are attempting to update is not a draft!");

	private String defaultMessage;

	UpdateHomeworkDraftResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

}

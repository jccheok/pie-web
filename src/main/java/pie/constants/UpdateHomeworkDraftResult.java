package pie.constants;

public enum UpdateHomeworkDraftResult {

	SUCCESS("Homework successfully updated."), FAIL_TO_UPDATE_HOMEWORK("Fail to update homework"), EMPTY_FIELD(
			"Some field is empty, please fill in the empty field before submitting");

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

package pie.constants;

public enum AddChildResult {
	
	SUCCESS("Added child successfully!"), 
	INVALID_STUDENT_CODE("The student code you've entered is unrecognized!"),
	CHILD_ALREADY_ADDED("You've already added that child!");

	private String defaultMessage;

	AddChildResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

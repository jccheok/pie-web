package pie.constants;

public enum AddChildResult {
	
	SUCCESS("Added Child successfully!"), 
	WRONG_STUDENT_CODE("The student code you've entered is wrong!"),
	CHILD_ALREADY_ADDED("The child you are attempting to add is already added");

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

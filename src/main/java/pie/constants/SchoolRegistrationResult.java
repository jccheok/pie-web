package pie.constants;

public enum SchoolRegistrationResult {
	
	SUCCESS("School successfully registered."), 
	SCHOOL_CODE_TAKEN("The school code you have entered is already taken!");

	private String defaultMessage;

	SchoolRegistrationResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

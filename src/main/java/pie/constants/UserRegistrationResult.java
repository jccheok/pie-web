package pie.constants;

public enum UserRegistrationResult {

	SUCCESS("Registration Successful! Please follow the link we've sent to your email to verify your account."),
	EMAIL_TAKEN("The email you have entered is already taken!"),
	INVALID_SCHOOL_CODE("The school code you've entered is unrecognized!"),
	INVALID_STUDENT_CODE("The student code you've entered is unrecognized or is already registered!"), 
	STUDENT_REGISTERED("The student code you've entered is unrecognized or is already registered!");

	private String defaultMessage;

	UserRegistrationResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

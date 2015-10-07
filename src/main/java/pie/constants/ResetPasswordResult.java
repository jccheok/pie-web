package pie.constants;

public enum ResetPasswordResult {
	
	SUCCESS("Password has been reset. Please check your email."), 
	INVALID_ANSWER("The answer you have entered is wrong!");

	private String defaultMessage;

	ResetPasswordResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

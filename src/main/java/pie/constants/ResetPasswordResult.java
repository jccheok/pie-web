package pie.constants;

public enum ResetPasswordResult {
	
	SUCCESS("Your password has been reset. A temporary password has been sent to your email."), 
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

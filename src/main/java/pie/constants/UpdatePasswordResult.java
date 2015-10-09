package pie.constants;

public enum UpdatePasswordResult {

	SUCCESS("Successfully updated password!"), 
	PASSWORD_IS_NOT_UPDATED("Password is not updated"), 
	SAME_AS_OLD_PASSWORD("You have this password before!");

	private String defaultMessage;

	UpdatePasswordResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

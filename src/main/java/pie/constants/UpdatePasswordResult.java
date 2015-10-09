package pie.constants;

public enum UpdatePasswordResult {

	SUCCESS("Successfully updated password!"), 
	PASSWORD_IS_NOT_UPDATED("Password is not updated"), 
	SAME_AS_OLD_PASSWORD("You have this password before!"),
	OLD_PASSWORD_DOES_NOT_MATCH("The password you have entered does not match your old password!");

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

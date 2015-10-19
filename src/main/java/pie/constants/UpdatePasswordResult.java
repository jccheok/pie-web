package pie.constants;

public enum UpdatePasswordResult {

	SUCCESS("Successfully updated password!"),
	SAME_AS_OLD_PASSWORD("Your new password must not be the same as the last two passwords you've set before!"),
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

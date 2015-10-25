package pie.constants;

public enum UpdateAccountResult {
	
	SUCCESS("Account successfully updated."),
	AUTH_TOKEN_FAIL("User authentication failed.");

	private String defaultMessage;

	UpdateAccountResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	
}

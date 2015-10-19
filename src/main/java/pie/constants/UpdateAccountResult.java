package pie.constants;

public enum UpdateAccountResult {
	
	SUCCESS("Account successfully updated!"),
	ACCOUNT_UPDATE_FAILED("The account failed to update!"), 
	ADDRESS_FAILED_TO_UPDATE("The address failed to update"),
	AUTH_TOKEN_FAIL("Failed to verify auth token");

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

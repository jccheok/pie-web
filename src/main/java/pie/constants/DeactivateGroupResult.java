package pie.constants;

public enum DeactivateGroupResult {
	
	SUCCESS("Successfully deactivated group."), 
	UNAUTHORIZED("You are not the owner of this group."),
	AUTH_TOKEN_FAIL("User authentication failed.");

	private String defaultMessage;

	DeactivateGroupResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

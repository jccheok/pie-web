package pie.constants;

public enum LeaveGroupResult {
	
	SUCCESS("Successfully left group."), 
	ALREADY_LEFT("You already left the group."), 
	INVALID_GROUP("The group you're trying to leave is invalid!");

	private String defaultMessage;

	LeaveGroupResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

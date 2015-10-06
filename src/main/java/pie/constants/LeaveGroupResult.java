package pie.constants;

public enum LeaveGroupResult {
	
	SUCCESS("Successfully left group."), 
	NOT_MEMBER("You are not a member of this group!");

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

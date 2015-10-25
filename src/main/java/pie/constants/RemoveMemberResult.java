package pie.constants;

public enum RemoveMemberResult {
	
	SUCCESS("Successfully removed from group."), 
	NOT_MEMBER("Invalid request. The user you're removing is not or no longer is a member of this group.");

	private String defaultMessage;

	RemoveMemberResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}
package pie.constants;

public enum JoinGroupResult {

	SUCCESS("Successfully joined group."), 
	INVALID_GROUP_CODE("The group code you've entered is unrecognized!"), 
	MISSING_GROUP_CODE("The group code you've entered is unrecognized!"), 
	GROUP_CLOSED("The group is currently closed."), 
	INVALID_GROUP("The group you're trying to join is invalid!"),
	ALREADY_MEMBER("You are already a member of this group!");

	private String defaultMessage;

	JoinGroupResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

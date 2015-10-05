package pie.constants;

public enum DeactivateGroupResult {
	SUCCESS("Successfully deactivated group."), 
	WRONG_PASSWORD("The password you have entered is wrong!"),
	GROUP_IS_NOT_VALID("The group is already deleted!"),
	INVALID_USER("You are not a valid user!"),
	GENERAL_FAILURE("General Failure in removing members");

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

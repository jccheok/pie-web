package pie.constants;

public enum TransferGroupOwnershipResult {

	SUCCESS("Successfully transferred ownership."), 
	WRONG_PASSWORD("The password you have entered is wrong!"), 
	USER_NOT_IN_GROUP("The staff you are attempting to add is not in the group!"),
	GROUP_IS_NOT_VALID("The group you choose is not valid!"),
	INVALID_USER("The email you entered is not valid!");

	private String defaultMessage;

	TransferGroupOwnershipResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

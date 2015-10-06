package pie.constants;

public enum TransferGroupOwnershipResult {

	SUCCESS("Successfully transferred group ownership."), 
	WRONG_PASSWORD("Wrong password!"),
	INVALID_TRANSFEREE("The staff email you have entered is invalid!");

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

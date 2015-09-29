package pie.constants;

public enum GroupRegistrationResult {
	
	SUCCESS("Group successfully registered."), 
	NAME_TAKEN("The group name you have entered is already taken!"), 
	GROUP_CODE_TAKEN("The group code you have entered is already taken!");

	private String defaultMessage;

	GroupRegistrationResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

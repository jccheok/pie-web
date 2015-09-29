package pie.constants;

public enum RelationshipRegistrationResult {

	SUCCESS("Relationship type registered."),
	NAME_TAKEN("The relationship name you have entered is already taken!");

	private String defaultMessage;

	RelationshipRegistrationResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

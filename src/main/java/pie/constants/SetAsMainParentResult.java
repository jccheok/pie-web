package pie.constants;

public enum SetAsMainParentResult {
	
	SUCCESS("You are now the main parent of this child!"), 
	NO_RELATIONSHIP("You are not the parent of this child!"),
	ALREADY_MAIN("You are already the main parent of this child!");

	private String defaultMessage;

	SetAsMainParentResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

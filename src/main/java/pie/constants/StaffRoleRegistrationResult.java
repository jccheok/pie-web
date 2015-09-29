package pie.constants;

public enum StaffRoleRegistrationResult {
	
	SUCCESS("Staff role registered."), 
	NAME_TAKEN("The staff role name you've entered is already taken!"), 
	OWNER_EXISTS("An owner staff role already exists!");

	private String defaultMessage;

	StaffRoleRegistrationResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

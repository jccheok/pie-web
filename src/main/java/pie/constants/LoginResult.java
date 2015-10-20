package pie.constants;

public enum LoginResult {
	
	SUCCESS("Login success!"), 
	NOT_VERIFIED("Please verify your email account."), 
	NOT_MATCHING("Incorrect email or password!"), 
	NOT_VALID("Your account is currently invalidated."), 
	NOT_REGISTERED("Incorrect email or password!"),
	PLATFORM_UNSUPPORTED("The platform you're trying to access is currently unsupported."),
	PASSWORD_EXPIRED("Your password has expired after 90 days! Please reset your password!");

	private String defaultMessage;

	LoginResult(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String toString() {
		return this.name();
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}

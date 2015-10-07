package pie;

public class SecurityQuestion {
	
	private int securityQuestionID;
	private String securityQuestionDescription;
	
	public SecurityQuestion(int securityQuestionID, String securityQuestionDescription){
		setSecurityQuestionID(securityQuestionID);
		setSecurityQuestionDescription(securityQuestionDescription);
	}
	
	public int getSecurityQuestionID() {
		return securityQuestionID;
	}
	public void setSecurityQuestionID(int securityQuestionID) {
		this.securityQuestionID = securityQuestionID;
	}
	public String getSecurityQuestionDescription() {
		return securityQuestionDescription;
	}
	public void setSecurityQuestionDescription(String securityQuestionDescription) {
		this.securityQuestionDescription = securityQuestionDescription;
	}

}

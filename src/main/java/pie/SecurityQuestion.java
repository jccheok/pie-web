package pie;

public class SecurityQuestion {
	
	private int securityQuestionID;
	private String securityQuesitonDescription;
	
	public SecurityQuestion(int securityQuestionID, String securityQuestionDescription){
		setSecurityQuestionID(securityQuestionID);
		setSecurityQuesitonDescription(securityQuestionDescription);
	}
	
	public int getSecurityQuestionID() {
		return securityQuestionID;
	}
	public void setSecurityQuestionID(int securityQuestionID) {
		this.securityQuestionID = securityQuestionID;
	}
	public String getSecurityQuesitonDescription() {
		return securityQuesitonDescription;
	}
	public void setSecurityQuesitonDescription(String securityQuesitonDescription) {
		this.securityQuesitonDescription = securityQuesitonDescription;
	}

}

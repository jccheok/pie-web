package pie;

import java.util.Date;

public class User {

	private int userID;
	private Address userAddress;
	private String userFirstName;
	private String userLastName;
	private UserType userType;
	private String userEmail;
	private String userPassword;
	private String userMobile;
	private boolean userIsValid;
	private boolean userIsVerified;
	private Date userLastLogin;
	private Date userRegistrationDate;
	private Date userLastUpdate;
	private SecurityQuestion userSecurityQuestion;
	private String userSecurityAnswer;
	private String userLastPassword1, userLastPassword2;
	private Date userPasswordLastUpdate;
	public User() {}
	
	public User(int userID, Address userAddress, String userFirstName,
			String userLastName, UserType userType, String userEmail,
			String userPassword, String userMobile, boolean userIsValid,
			boolean userIsVerified, Date userLastLogin,
			Date userRegistrationDate, Date userLastUpdate, SecurityQuestion userSecurityQuestion, 
			String userSecurityAnswer, String userLastPassword1, String userLastPassword2, Date userPasswordLastUpdate) {
		
		setUserID(userID);
		setUserAddress(userAddress);
		setUserFirstName(userFirstName);
		setUserLastName(userLastName);
		setUserType(userType);
		setUserEmail(userEmail);
		setUserPassword(userPassword);
		setUserMobile(userMobile);
		setUserIsValid(userIsValid);
		setUserIsVerified(userIsVerified);
		setUserLastLogin(userLastLogin);
		setUserRegistrationDate(userRegistrationDate);
		setUserLastUpdate(userLastUpdate);
		setUserSecurityQuestion(userSecurityQuestion);
		setUserSecurityAnswer(userSecurityAnswer);
		setUserLastPassword1(userLastPassword1);
		setUserLastPassword2(userLastPassword2);
		setUserPasswordLastUpdate(userPasswordLastUpdate);
		
	}
	
	public User(User user) {
		
		setUserID(user.getUserID());
		setUserAddress(user.getUserAddress());
		setUserFirstName(user.getUserFirstName());
		setUserLastName(user.getUserLastName());
		setUserType(user.getUserType());
		setUserEmail(user.getUserEmail());
		setUserPassword(user.getUserPassword());
		setUserMobile(user.getUserMobile());
		setUserIsValid(user.userIsValid());
		setUserIsVerified(user.userIsVerified());
		setUserLastLogin(user.getUserLastLogin());
		setUserRegistrationDate(user.getUserRegistrationDate());
		setUserLastUpdate(user.getUserLastUpdate());
		setUserSecurityQuestion(user.getUserSecurityQuestion());
		setUserSecurityAnswer(user.getUserSecurityAnswer());
		setUserLastPassword1(user.getUserLastPassword1());
		setUserLastPassword2(user.getUserLastPassword2());
		setUserPasswordLastUpdate(user.getUserPasswordLastUpdate());
		
	}	

	public int getUserID() {
		return userID;
	}

	public Address getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(Address userAddress) {
		this.userAddress = userAddress;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public String getUserFullName() {
		return userFirstName + " " + userLastName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public boolean userIsValid() {
		return userIsValid;
	}

	public void setUserIsValid(boolean userIsValid) {
		this.userIsValid = userIsValid;
	}

	public boolean userIsVerified() {
		return userIsVerified;
	}

	public void setUserIsVerified(boolean userIsVerified) {
		this.userIsVerified = userIsVerified;
	}

	public Date getUserRegistrationDate() {
		return userRegistrationDate;
	}

	public void setUserRegistrationDate(Date userRegistrationDate) {
		this.userRegistrationDate = userRegistrationDate;
	}

	public Date getUserLastUpdate() {
		return userLastUpdate;
	}

	public void setUserLastUpdate(Date userLastUpdate) {
		this.userLastUpdate = userLastUpdate;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Date getUserLastLogin() {
		return userLastLogin;
	}

	public void setUserLastLogin(Date userLastLogin) {
		this.userLastLogin = userLastLogin;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public User toUser() {
		return (User) this;
	}

	public SecurityQuestion getUserSecurityQuestion() {
		return userSecurityQuestion;
	}

	public void setUserSecurityQuestion(SecurityQuestion userSecurityQuestion) {
		this.userSecurityQuestion = userSecurityQuestion;
	}

	public String getUserSecurityAnswer() {
		return userSecurityAnswer;
	}

	public void setUserSecurityAnswer(String userSecurityAnswer) {
		this.userSecurityAnswer = userSecurityAnswer;
	}

	public String getUserLastPassword2() {
		return userLastPassword2;
	}

	public void setUserLastPassword2(String userLastPassword2) {
		this.userLastPassword2 = userLastPassword2;
	}

	public String getUserLastPassword1() {
		return userLastPassword1;
	}

	public void setUserLastPassword1(String userLastPassword1) {
		this.userLastPassword1 = userLastPassword1;
	}

	public Date getUserPasswordLastUpdate() {
		return userPasswordLastUpdate;
	}

	public void setUserPasswordLastUpdate(Date userPasswordLastUpdate) {
		this.userPasswordLastUpdate = userPasswordLastUpdate;
	}
}

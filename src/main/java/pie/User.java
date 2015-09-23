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
}

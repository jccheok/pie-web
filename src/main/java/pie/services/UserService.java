package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Address;
import pie.SecurityQuestion;
import pie.User;
import pie.UserType;
import pie.constants.LoginResult;
import pie.constants.ResetPasswordResult;
import pie.constants.SupportedPlatform;
import pie.constants.UpdateAccountResult;
import pie.constants.UpdatePasswordResult;
import pie.utilities.DatabaseConnector;

public class UserService {

	public boolean isRegisteredUser(String userEmail) {

		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `User` WHERE emailAddress = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			isRegistered = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isRegistered;
	}

	public boolean isVerifiedUser(String userEmail) {

		boolean isVerified = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isVerified FROM `User` WHERE emailAddress = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				isVerified = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isVerified;
	}

	public boolean verifyUser(String userEmail) {

		boolean verifyResult = true;

		if (!isValidUser(userEmail)) {
			verifyResult = false;
		} else {
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `User` SET isVerified = ? WHERE emailAddress = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 1);
				pst.setString(2, userEmail);
				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return verifyResult;
	}

	public boolean isValidUser(String userEmail) {

		boolean isValid = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isValid FROM `User` WHERE emailAddress = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isValid = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	public boolean credentialsMatch(String userEmail, String userPassword) {
		boolean matches = false;

		if (userPassword.length() == 64) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "SELECT * FROM `User` WHERE emailAddress = ? AND password = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);

				matches = pst.executeQuery().next();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return matches;
	}

	public LoginResult loginUser(String userEmail, String userPassword, SupportedPlatform platform) {

		LoginResult loginResult = LoginResult.SUCCESS;

		if (!isRegisteredUser(userEmail)) {
			loginResult = LoginResult.NOT_REGISTERED;
		} else if (!credentialsMatch(userEmail, userPassword)) {
			loginResult = LoginResult.NOT_MATCHING;
		} else {

			User user = getUser(getUserID(userEmail));

			if (!platform.supportsUserType(user.getUserType())) {
				loginResult = LoginResult.PLATFORM_UNSUPPORTED;
			} else if (!isVerifiedUser(userEmail)) {
				loginResult = LoginResult.NOT_VERIFIED;
			} else if (!isValidUser(userEmail)) {
				loginResult = LoginResult.NOT_VALID;
			} else {

				try {

					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;

					String sql = "UPDATE `User` SET lastLogin = CURRENT_TIMESTAMP() WHERE emailAddress = ?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, userEmail);
					pst.executeUpdate();

					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return loginResult;
	}

	public int getUserID(String userEmail) {

		int userID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userID FROM `User` WHERE emailAddress = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				userID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userID;
	}

	public User getUser(int userID) {

		AddressService addressService = new AddressService();
		SecurityQuestionService securityQuestionService = new SecurityQuestionService();
		User user = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `User` WHERE userID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				UserType userType = UserType.getUserType(resultSet.getInt("userTypeID"));
				Address userAddress = addressService.getAddress(resultSet.getInt("addressID"));
				String userFirstName = resultSet.getString("firstName");
				String userLastName = resultSet.getString("lastName");
				String userEmail = resultSet.getString("emailAddress");
				String userPassword = resultSet.getString("password");
				String userMobile = resultSet.getString("mobile");
				boolean userIsVerified = resultSet.getInt("isVerified") == 1;
				boolean userIsValid = resultSet.getInt("isValid") == 1;
				Date userLastLogin = new Date(resultSet.getTimestamp("lastLogin").getTime());
				Date userRegistrationDate = new Date(resultSet.getTimestamp("registrationDate").getTime());
				Date userLastUpdate = new Date(resultSet.getTimestamp("lastUpdate").getTime());
				SecurityQuestion userSecurityQuestion = securityQuestionService.getSecurityQuestion(resultSet
						.getInt("securityQuestionID"));
				String userSecurityAnswer = resultSet.getString("securityQuestionAnswer");
				String userLastPassword1 = resultSet.getString("passwordLast1");
				String userLastPassword2 = resultSet.getString("passwordLast2");
				Date userPasswordLastUpdate = new Date(resultSet.getTimestamp("passwordLastUpdate").getTime());

				user = new User(userID, userAddress, userFirstName, userLastName, userType, userEmail, userPassword,
						userMobile, userIsValid, userIsVerified, userLastLogin, userRegistrationDate, userLastUpdate,
						userSecurityQuestion, userSecurityAnswer, userLastPassword1, userLastPassword2,
						userPasswordLastUpdate);

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
	
	private void setNewPassword(int userID, String newUserPassword) {
		
		User user = getUser(userID);
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `User` SET password = ?, passwordLastUpdate = NOW(), passwordLast1 = ?, passwordLast2 = ?, lastUpdate = NOW() WHERE userID = ?";
			pst = conn.prepareStatement(sql);
			
			pst.setString(1, newUserPassword);
			pst.setString(2, user.getUserPassword());
			pst.setString(3, user.getUserLastPassword1());
			pst.setInt(4, userID);
			
			pst.executeUpdate();
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ResetPasswordResult resetPassword(int userID, String securityQuestionAnswer, String newPassword) {
		
		ResetPasswordResult resetPasswordResult = ResetPasswordResult.SUCCESS;
		
		User user = getUser(userID);

		if (!user.getUserSecurityAnswer().equals(securityQuestionAnswer)) {
			resetPasswordResult = ResetPasswordResult.INVALID_ANSWER;
		} else {
			
			setNewPassword(userID, newPassword);
		}

		return resetPasswordResult;
	}

	public UpdatePasswordResult updatePassword(int userID, String newUserPassword, String oldUserPassword) {
		
		UpdatePasswordResult updatePasswordResult = UpdatePasswordResult.SUCCESS;

		User user = getUser(userID);
		
		if (!user.getUserPassword().equals(oldUserPassword)) {
			updatePasswordResult = UpdatePasswordResult.OLD_PASSWORD_DOES_NOT_MATCH;
		} else if (user.getUserLastPassword1().equals(newUserPassword)) {
			updatePasswordResult = UpdatePasswordResult.SAME_AS_OLD_PASSWORD;
		} else if (user.getUserLastPassword2().equals(newUserPassword)) {
			updatePasswordResult = UpdatePasswordResult.SAME_AS_OLD_PASSWORD;
		} else {
			setNewPassword(userID, newUserPassword);
		}

		return updatePasswordResult;
	}

	public UpdateAccountResult updateUserAccountDetails(int userID, String userMobile, int securityQuestionID,
			String securityQuestionAnswer, String addressStreet, String addressPostalCode, int cityID, String authToken){

		UpdateAccountResult updateAccountResult = UpdateAccountResult.SUCCESS;
		AuthService authService = new AuthService();

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			String sql = null;
			if(authToken.equals(authService.getAuthToken(userID))){
				if (addressPostalCode == null) {
					sql = "UPDATE `User` SET mobile = ?, securityQuestionID = ?, securityQuestionAnswer = ? WHERE userID = ?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, userMobile);
					pst.setInt(2, securityQuestionID);
					pst.setString(3, securityQuestionAnswer);
					pst.setInt(4, userID);

					if (pst.executeUpdate() == 0) {
						updateAccountResult = UpdateAccountResult.ACCOUNT_UPDATE_FAILED;
					}
				} else {
					AddressService addressService = new AddressService();
					int addressID = addressService.getAddressID(addressPostalCode);

					if (addressID != -1) {
						sql = "UPDATE `User` SET mobile = ?, securityQuestionID = ?, securityQuestionAnswer = ?, addressID = ? WHERE userID = ?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, userMobile);
						pst.setInt(2, securityQuestionID);
						pst.setString(3, securityQuestionAnswer);
						pst.setInt(4, addressID);
						pst.setInt(5, userID);

						if (pst.executeUpdate() == 0) {
							updateAccountResult = UpdateAccountResult.ACCOUNT_UPDATE_FAILED;
						}
					} else {
						addressID = addressService.registerAddress(addressPostalCode, addressStreet, cityID);
						
						sql = "UPDATE `User` SET mobile = ?, securityQuestionID = ?, securityQuestionAnswer = ?, addressID = ? WHERE userID = ?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, userMobile);
						pst.setInt(2, securityQuestionID);
						pst.setString(3, securityQuestionAnswer);
						pst.setInt(4, addressID);
						pst.setInt(5, userID);

						if (pst.executeUpdate() == 0) {
							updateAccountResult = UpdateAccountResult.ACCOUNT_UPDATE_FAILED;
						}
					}

				}
			}else{
				updateAccountResult = UpdateAccountResult.AUTH_TOKEN_FAIL;
			}
		
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return updateAccountResult;
	}
}


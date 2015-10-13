package pie.services;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.http.HttpServlet;

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
import pie.utilities.Utilities;

public class UserService {

	public boolean isRegisteredUser(String userEmail) {

		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `User` WHERE userEmail = ?";
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

			String sql = "SELECT userIsVerified FROM `User` WHERE userEmail = ?";
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

				String sql = "UPDATE `User` SET userIsVerified = ? WHERE userEmail = ?";
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

			String sql = "SELECT userIsValid FROM `User` WHERE userEmail = ?";
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

				String sql = "SELECT * FROM `User` WHERE userEmail = ? AND userPassword = ?";
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

					String sql = "UPDATE `User` SET userLastLogin = CURRENT_TIMESTAMP() WHERE userEmail = ?";
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

			String sql = "SELECT userID FROM `User` WHERE userEmail = ?";
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
				String userFirstName = resultSet.getString("userFirstName");
				String userLastName = resultSet.getString("userLastName");
				String userEmail = resultSet.getString("userEmail");
				String userPassword = resultSet.getString("userPassword");
				String userMobile = resultSet.getString("userMobile");
				boolean userIsVerified = resultSet.getInt("userIsVerified") == 1;
				boolean userIsValid = resultSet.getInt("userIsValid") == 1;
				Date userLastLogin = new Date(resultSet.getTimestamp("userLastLogin").getTime());
				Date userRegistrationDate = new Date(resultSet.getTimestamp("userRegistrationDate").getTime());
				Date userLastUpdate = new Date(resultSet.getTimestamp("userLastUpdate").getTime());
				SecurityQuestion userSecurityQuestion = securityQuestionService.getSecurityQuestion(resultSet
						.getInt("securityQuestionID"));
				String userSecurityAnswer = resultSet.getString("securityQuestionAnswer");
				String userLastPassword1 = resultSet.getString("userLastPassword1");
				String userLastPassword2 = resultSet.getString("userLastPassword2");
				Date userPasswordLastUpdate = new Date(resultSet.getTimestamp("userPasswordLastUpdate").getTime());

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

	public boolean setNewPassword(int userID, String userPassword) {
		boolean setPasswordResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `User` SET userPassword = SHA2(? , 256), userLastUpdate = NOW() WHERE userID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userPassword);
			pst.setInt(2, userID);
			pst.executeUpdate();

			conn.close();
			setPasswordResult = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return setPasswordResult;
	}

	public ResetPasswordResult resetPassword(int userID, String securityQuestionAnswer, HttpServlet httpServlet) {
		ResetPasswordResult resetPasswordResult = ResetPasswordResult.SUCCESS;

		User user = getUser(userID);
		EmailService emailService = new EmailService();

		if (user.getUserSecurityAnswer().equals(securityQuestionAnswer)) {
			try {
				String newPassword = Utilities.generateString(10);
				String loginLink = "http://piedev-rpmaps.rhcloud.com/servlets/servlets/login";
				InputStream emailTemplateStream = httpServlet.getServletContext().getResourceAsStream(
						"/resources/resetPasswordTemplate.html");

				String emailSubject = "Account Password Reset on Partners in Education";
				String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);

				String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", getUser(userID).getUserFirstName());
				emailContent = emailContent.replaceAll("\\$PASSWORD", newPassword);
				emailContent = emailContent.replaceAll("\\$LOGIN_LINK", loginLink);

				if (!setNewPassword(userID, newPassword)) {
					resetPasswordResult = ResetPasswordResult.RESET_FAILED;
				} else {
					emailService.sendEmail(emailSubject, emailContent, new String[] { getUser(userID).getUserEmail() });
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			resetPasswordResult = ResetPasswordResult.INVALID_ANSWER;
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
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `User` SET userPassword = ?, userPasswordLastUpdate = NOW(), userLastPassword1 = ?, userLastPassword2 = ?, userLastUpdate = NOW() WHERE userID = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, newUserPassword);
				pst.setString(2, user.getUserLastPassword2());
				pst.setString(3, user.getUserPassword());
				pst.setInt(4, userID);

				if (pst.executeUpdate() == 0) {
					updatePasswordResult = UpdatePasswordResult.PASSWORD_IS_NOT_UPDATED;
				}

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return updatePasswordResult;
	}

	public UpdateAccountResult updateUserAccountDetails(int userID, String userFirstName, String userLastName,
			String userMobile, int securityQuestionID,
			String securityQuestionAnswer, String addressStreet, String addressPostalCode, int cityID) {

		UpdateAccountResult updateAccountResult = UpdateAccountResult.SUCCESS;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			String sql = null;

			if (addressStreet == null) {
				sql = "UPDATE `User` SET userMobile = ?,  userFirstName = ?, userLastName = ?, securityQuestionID = ?, securityQuestionAnswer = SHA2(? , 256) WHERE userID = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userMobile);
				pst.setString(2, userFirstName);
				pst.setString(3, userLastName);
				pst.setInt(4, securityQuestionID);
				pst.setString(5, securityQuestionAnswer);
				pst.setInt(6, userID);

				if (pst.executeUpdate() == 0) {
					updateAccountResult = UpdateAccountResult.ACCOUNT_UPDATE_FAILED;
				}
			} else {
				AddressService addressService = new AddressService();
				int addressID = addressService.registerAddress(addressPostalCode, addressStreet, cityID);

				if (addressID != -1) {
					sql = "UPDATE `User` SET userMobile = ?,  userFirstName = ?, userLastName = ?, securityQuestionID = ?, securityQuestionAnswer = SHA2(? , 256), addressID = ? WHERE userID = ?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, userMobile);
					pst.setString(2, userFirstName);
					pst.setString(3, userLastName);
					pst.setInt(4, securityQuestionID);
					pst.setString(5, securityQuestionAnswer);
					pst.setInt(6, addressID);
					pst.setInt(7, userID);

					if (pst.executeUpdate() == 0) {
						updateAccountResult = UpdateAccountResult.ACCOUNT_UPDATE_FAILED;
					}
				} else {
					updateAccountResult = UpdateAccountResult.ADDRESS_FAILED_TO_UPDATE;
				}

			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return updateAccountResult;
	}
}

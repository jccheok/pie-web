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
				SecurityQuestion userSecurityQuestion = securityQuestionService.getSecurityQuestion(resultSet.getInt("securityQuestionID"));
				String userSecurityAnswer = resultSet.getString("securityQuestionAnswer");

				user = new User(userID, userAddress, userFirstName, userLastName, userType, userEmail, userPassword,
						userMobile, userIsValid, userIsVerified, userLastLogin, userRegistrationDate, userLastUpdate, userSecurityQuestion, userSecurityAnswer);

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
	
	public ResetPasswordResult resetPassword(int userID, String securityQuestionAnswer, HttpServlet httpServlet){
		ResetPasswordResult resetPasswordResult = ResetPasswordResult.SUCCESS;
		
		User user = getUser(userID);
		EmailService emailService = new EmailService();
		
		if(user.getUserSecurityAnswer().equals(securityQuestionAnswer)){
			try{
				String loginLink = "http://piedev-rpmaps.rhcloud.com/servlets/servlets/login";
				InputStream emailTemplateStream = httpServlet.getServletContext().getResourceAsStream("/resources/resetPasswordTemplate.html");
				
				String emailSubject = "Account Password Reset on Partners in Education";
				String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);

				String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", getUser(userID).getUserFirstName());
				emailContent = emailContent.replaceAll("\\$PASSWORD", Utilities.generateString(10));
				emailContent = emailContent.replaceAll("\\$LOGIN_LINK", loginLink);

				emailService.sendEmail(emailSubject, emailContent, new String[] { getUser(userID).getUserEmail() });
				
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			resetPasswordResult = ResetPasswordResult.INVALID_ANSWER;
		}
		
		return resetPasswordResult;
	}
}

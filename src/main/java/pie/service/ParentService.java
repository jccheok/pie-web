package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Parent;
import pie.User;
import pie.UserType;
import pie.util.DatabaseConnector;

public class ParentService {
	
	public enum RegistrationResult {
		SUCCESS("Registration Successful! Please follow the link we've sent to your email to verify your account."), 
		EMAIL_TAKEN("The email you have entered is already taken!");

		private String defaultMessage;
		
		RegistrationResult(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}
		
		public String toString() {
			return this.name();
		}
		
		public String getDefaultMessage() {
			return defaultMessage;
		}
	}
	
	public Parent getParent(int parentID) {
		UserService userService = new UserService();
		
		Parent parent = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Parent` WHERE parentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, parentID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				User user = userService.getUser(parentID);
				parent = new Parent(user);
				
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return parent;
	}
	
	public RegistrationResult registerParent(String userFirstName,
			String userLastName, String userEmail, String userPassword,
			String userMobile) {
		
		UserService userService = new UserService();
		
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;
		
		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = RegistrationResult.EMAIL_TAKEN;
		}
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile) VALUES (?, ?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, UserType.PARENT.getUserTypeID());
			pst.setString(2, userFirstName);
			pst.setString(3, userLastName);
			pst.setString(4, userEmail);
			pst.setString(5, userPassword);
			pst.setString(6, userMobile);
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if (resultSet.next()) {
				
				int newUserID = resultSet.getInt(1);
				
				sql = "INSERT INTO `Parent` (parentID) VALUES (?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, newUserID);
				pst.executeUpdate();
				
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return registrationResult;
	}

}

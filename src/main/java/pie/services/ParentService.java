package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Parent;
import pie.User;
import pie.UserType;
import pie.constants.UserRegistrationResult;
import pie.utilities.DatabaseConnector;

public class ParentService {

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
			e.printStackTrace();
		}

		return parent;
	}

	public UserRegistrationResult registerParent(String userFirstName, String userLastName, String userEmail,
			String userPassword, String userMobile, int securityQuestionID, String securityQuesitonAnswer) {

		UserService userService = new UserService();
		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else {
			
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT INTO `User` (userTypeID, firstName, lastName, email, password, mobile, securityQuestionID, securityQuestionAnswer, passwordLastUpdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, UserType.PARENT.getUserTypeID());
				pst.setString(2, userFirstName);
				pst.setString(3, userLastName);
				pst.setString(4, userEmail);
				pst.setString(5, userPassword);
				pst.setString(6, userMobile);
				pst.setInt(7, securityQuestionID);
				pst.setString(8, securityQuesitonAnswer);
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
				e.printStackTrace();
			}
		}

		return registrationResult;
	}

}

package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Address;
import pie.User;
import pie.UserType;
import pie.util.DatabaseConnector;

public class UserService {

	public enum LoginResult {
		SUCCESS, NOT_VERIFIED, NOT_MATCHING, NOT_VALID, NOT_REGISTERED;

		public String toString() {
			return this.name();
		}
	}

	public boolean isRegisteredUser(String userEmail) {

		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			isRegistered = pst.executeQuery().next();
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isRegistered;
	}

	public boolean isVerifiedUser(String userEmail) {
		boolean isVerified = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isVerified FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isVerified = resultSet.getInt(1) == 1;
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isVerified;
	}

	public boolean verifyUser(String userEmail) {
		boolean verifyResult = true;

		if (isValidUser(userEmail)) {
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
				System.out.println(e);
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

			String sql = "SELECT isValid FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isValid = resultSet.getInt(1) == 1;
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isValid;
	}

	public LoginResult loginUser(String userEmail, String userPassword) {
		LoginResult loginResult = LoginResult.SUCCESS;

		if (isRegisteredUser(userEmail)) {
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "SELECT * FROM `User` WHERE userEmail AND userPassword = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);

				if (pst.executeQuery().next()) {

					if (isVerifiedUser(userEmail)) {
						if (isValidUser(userEmail)) {

							sql = "UPDATE `User` SET userLastLogin = NOW() WHERE userEmail = ?";
							pst = conn.prepareStatement(sql);
							pst.setString(1, userEmail);
							pst.executeUpdate();

						} else {
							loginResult = LoginResult.NOT_VALID;
						}
					} else {
						loginResult = LoginResult.NOT_VERIFIED;
					}
				} else {
					loginResult = LoginResult.NOT_MATCHING;
				}
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			loginResult = LoginResult.NOT_REGISTERED;
		}

		return loginResult;
	}

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
				user = new User();
				user.setUserID(userID);
				user.setUserType(UserType.getUserType(resultSet
						.getInt("userTypeID")));
				user.setUserAddress(new AddressService().getAddress(resultSet
						.getInt("addressID")));
				user.setUserFirstName(resultSet.getString("userFirstName"));
				user.setUserLastName(resultSet.getString("userLastName"));
				user.setUserEmail(resultSet.getString("userEmail"));
				user.setUserPassword(resultSet.getString("userPassword"));
				user.setUserMobile(resultSet.getString("userMobile"));
				user.setUserIsVerified(resultSet.getInt("userIsVerified") == 1);
				user.setUserIsValid(resultSet.getInt("userIsValid") == 1);
				user.setUserLastLogin(new Date(resultSet.getTimestamp(
						"userLastLogin").getTime()));
				user.setUserRegistrationDate(new Date(resultSet.getTimestamp(
						"userRegistrationDate").getTime()));
				user.setUserLastUpdate(new Date(resultSet.getTimestamp(
						"userLastUpdate").getTime()));
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return user;
	}

	public int registerUser(UserType userType, Address userAddress,
			String userFirstName, String userLastName, String userPassword,
			String userMobile) {
		int userID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `User` (userTypeID, addressID, userFirstName, userLastName, userEmail, userPassword, userMobile) VALUES (?, ?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, userType.getUserTypeID());
			pst.setInt(2, userAddress.getAddressID());
			pst.setString(3, userFirstName);
			pst.setString(4, userLastName);
			pst.setString(5, userPassword);
			pst.setString(6, userMobile);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				userID = resultSet.getInt(1);
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return userID;
	}

}

package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Address;
import pie.User;
import pie.UserType;
import pie.constants.LoginResult;
import pie.constants.SupportedPlatform;
import pie.utilities.DatabaseConnector;

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

			String sql = "SELECT isVerified FROM `User` WHERE userEmail = ?";
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

	public boolean verifyUser(User user) {

		boolean verifyResult = true;

		String userEmail = user.getUserEmail();
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

			String sql = "SELECT isValid FROM `User` WHERE userEmail = ?";
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

	public LoginResult loginUser(String userEmail, String userPassword, SupportedPlatform platform) {

		LoginResult loginResult = LoginResult.SUCCESS;

		if (!isRegisteredUser(userEmail)) {
			loginResult = LoginResult.NOT_REGISTERED;
		} else {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "SELECT userTypeID FROM `User` WHERE userEmail = ? AND userPassword = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);
				resultSet = pst.executeQuery();

				if (resultSet.next()) {

					UserType userType = UserType.getUserType(resultSet.getInt(1));
					if (!platform.supportsUserType(userType)) {
						loginResult = LoginResult.PLATFORM_UNSUPPORTED;
					} else if (!isVerifiedUser(userEmail)) {
						loginResult = LoginResult.NOT_VERIFIED;
					} else if (!isValidUser(userEmail)) {
						loginResult = LoginResult.NOT_VALID;
					} else {

						sql = "UPDATE `User` SET userLastLogin = NOW() WHERE userEmail = ?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, userEmail);
						pst.executeUpdate();
					}
				} else {
					loginResult = LoginResult.NOT_MATCHING;
				}

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
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
			System.out.println(e);
		}

		return userID;
	}

	public User getUser(int userID) {

		AddressService addressService = new AddressService();
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

				user = new User(userID, userAddress, userFirstName, userLastName, userType, userEmail, userPassword,
						userMobile, userIsValid, userIsVerified, userLastLogin, userRegistrationDate, userLastUpdate);

			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return user;
	}
}

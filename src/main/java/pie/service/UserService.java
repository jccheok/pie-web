package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Country;
import pie.User;
import pie.UserType;

public class UserService extends DatabaseService {

	public enum LoginResult {
		SUCCESS, NOT_VERIFIED, NOT_MATCHING, NOT_VALID, NOT_REGISTERED;
		
		public String toString() {
			return this.name();
		}
	}

	private Connection conn;

	public UserService(Connection conn) {
		super(conn);
	}

	public boolean isRegistered(String userEmail) {

		boolean isRegistered = false;

		try {

			PreparedStatement pst = null;

			String sql = "SELECT * FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			isRegistered = pst.executeQuery().next();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isRegistered;
	}

	public boolean isVerified(String userEmail) {
		boolean isVerified = false;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isVerified FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isVerified = resultSet.getInt(1) == 1;
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return isVerified;
	}

	public boolean isValid(String userEmail) {
		boolean isValid = false;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isValid FROM `User` WHERE userEmail";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userEmail);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isValid = resultSet.getInt(1) == 1;
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return isValid;
	}

	public LoginResult login(String userEmail, String userPassword) {
		LoginResult loginResult = LoginResult.SUCCESS;

		if (isRegistered(userEmail)) {
			try {

				PreparedStatement pst = null;

				String sql = "SELECT * FROM `User` WHERE userEmail AND userPassword = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);

				if (pst.executeQuery().next()) {

					if (isVerified(userEmail)) {
						if (isValid(userEmail)) {

						} else {
							loginResult = LoginResult.NOT_VALID;
						}
					} else {
						loginResult = LoginResult.NOT_VERIFIED;
					}
				} else {
					loginResult = LoginResult.NOT_MATCHING;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			loginResult = LoginResult.NOT_REGISTERED;
		}

		return loginResult;
	}

	public User getUser(int userID) {
		User user = null;

		try {

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
				user.setUserAddress(new AddressService(conn)
						.getAddress(resultSet.getInt("addressID")));
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

		} catch (Exception e) {
			System.out.println(e);
		}

		return user;
	}

}

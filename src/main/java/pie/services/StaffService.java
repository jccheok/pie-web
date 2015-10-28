package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.School;
import pie.Staff;
import pie.User;
import pie.UserType;
import pie.constants.UserRegistrationResult;
import pie.utilities.DatabaseConnector;

public class StaffService {

	public Staff getStaff(int staffID) {

		SchoolService schoolService = new SchoolService();
		UserService userService = new UserService();

		Staff staff = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Staff` WHERE staffID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				User user = userService.getUser(staffID);
				School staffSchool = schoolService.getSchool(resultSet.getInt("schoolID"));
				String staffDesignation = resultSet.getString("title");
				boolean staffIsSchoolAdmin = resultSet.getInt("isSchoolAdmin") == 1;

				staff = new Staff(user, staffSchool, staffDesignation, staffIsSchoolAdmin);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return staff;
	}

	public UserRegistrationResult registerStaff(String userFirstName, String userLastName, String userEmail,
			String userPassword, String userMobile, String schoolCode, String staffDesignation, int securityQuestionID, String securityQuestionAnswer) {

		UserService userService = new UserService();
		SchoolService schoolService = new SchoolService();

		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else if (schoolService.isAvailableSchoolCode(schoolCode)) {
			registrationResult = UserRegistrationResult.INVALID_SCHOOL_CODE;
		} else {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile, securityQuestionID, securityQuestionAnswer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, UserType.STAFF.getUserTypeID());
				pst.setString(2, userFirstName);
				pst.setString(3, userLastName);
				pst.setString(4, userEmail);
				pst.setString(5, userPassword);
				pst.setString(6, userMobile);
				pst.setInt(7, securityQuestionID);
				pst.setString(8, securityQuestionAnswer);
				pst.executeUpdate();

				resultSet = pst.getGeneratedKeys();

				if (resultSet.next()) {

					int newUserID = resultSet.getInt(1);

					sql = "INSERT INTO `Staff` (staffID, schoolID, staffDesignation) VALUES (?, ?, ?)";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, newUserID);
					pst.setInt(2, schoolService.getSchoolID(schoolCode));
					pst.setString(3, staffDesignation);
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

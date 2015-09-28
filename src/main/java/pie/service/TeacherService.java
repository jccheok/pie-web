package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.School;
import pie.Teacher;
import pie.TeacherRole;
import pie.User;
import pie.UserType;
import pie.util.DatabaseConnector;

public class TeacherService {

	public enum RegistrationResult {
		SUCCESS(
				"Registration Successful! Please follow the link we've sent to your email to verify your account."), EMAIL_TAKEN(
				"The email you have entered is already taken!"), INVALID_SCHOOL_CODE(
				"The school code you've entered is unrecognized!");

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

	public enum JoinGroupResult {
		SUCCESS("Successfully joined Group!"), GROUP_IS_NOT_OPEN(
				"The Group you are trying to join is not open"), GROUP_IS_NOT_VALID(
				"The Group you are attempting to join is not valid");

		private String defaultMessage;

		JoinGroupResult(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}

		public String toString() {
			return this.name();
		}

		public String getDefaultMessage() {
			return defaultMessage;
		}
	}

	public Teacher getTeacher(int teacherID) {
		
		SchoolService schoolService = new SchoolService();
		UserService userService = new UserService();
		
		Teacher teacher = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Teacher` WHERE teacherID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				User user = userService.getUser(teacherID);
				School teacherSchool = schoolService.getSchool(resultSet.getInt("schoolID"));
				String teacherTitle = resultSet.getString("teacherTitle");
				boolean teacherIsSchoolAdmin = resultSet.getInt("teacherIsSchoolAdmin") == 1;
				
				teacher = new Teacher(user, teacherSchool, teacherTitle, teacherIsSchoolAdmin);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacher;
	}
	
	public boolean isMember(int teacherID, int groupID) {
		boolean isMember = false;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `TeacherGroup` WHERE teacherID = ? AND groupID = ? AND teacherGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return isMember;
		
	}
	
	public boolean setTeacherRole(int teacherID, int groupID, TeacherRole teacherRole) {
		boolean setResult = false;
		
		if (isMember(teacherID, groupID)) {
			if (!getTeacherRole(teacherID, groupID).equals(teacherRole)) {
				
				try {
					
					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					
					String sql = "UPDATE `TeacherGroup` SET teacherRoleID = ? WHERE teacherID = ? AND groupID = ?";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, teacherRole.getTeacherRoleID());
					pst.setInt(2, teacherID);
					pst.setInt(3, groupID);
					pst.executeUpdate();
					
					setResult = true;
					
					conn.close();
					
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		
		return setResult;
	}
	
	public TeacherRole getTeacherRole(int teacherID, int groupID) {
		
		TeacherRoleService teacherRoleService = new TeacherRoleService();
		TeacherRole teacherRole = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherGroup` WHERE teacherID = ? AND groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);
			pst.setInt(2, groupID);
			resultSet = pst.executeQuery();
			
			if (resultSet.next()) {
				teacherRole = teacherRoleService.getTeacherRole(resultSet.getInt(1));
			}

			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return teacherRole;
	}

	public RegistrationResult registerTeacher(String userFirstName,
			String userLastName, String userEmail, String userPassword,
			String userMobile, String schoolCode, String teacherTitle) {
		
		UserService userService = new UserService();
		SchoolService schoolService = new SchoolService();
		
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = RegistrationResult.EMAIL_TAKEN;
		} else {
			if (!schoolService.isAvailableSchoolCode(schoolCode)) {
				
				try {

					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					ResultSet resultSet = null;

					String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile) VALUES (?, ?, ?, ?, ?, ?, ?)";
					pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, UserType.TEACHER.getUserTypeID());
					pst.setString(2, userFirstName);
					pst.setString(3, userLastName);
					pst.setString(4, userEmail);
					pst.setString(5, userPassword);
					pst.setString(6, userMobile);
					pst.executeUpdate();

					resultSet = pst.getGeneratedKeys();

					if (resultSet.next()) {

						int newUserID = resultSet.getInt(1);

						sql = "INSERT INTO `Teacher` (teacherID, schoolID, teacherTitle) VALUES (?, ?, ?)";
						pst = conn.prepareStatement(sql);
						pst.setInt(1, newUserID);
						pst.setInt(2, schoolService.getSchoolID(schoolCode));
						pst.setString(3, teacherTitle);
						pst.executeUpdate();

					}

					conn.close();

				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				registrationResult = RegistrationResult.INVALID_SCHOOL_CODE;
			}
		}

		return registrationResult;
	}

}

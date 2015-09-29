package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Group;
import pie.School;
import pie.Staff;
import pie.StaffRole;
import pie.User;
import pie.UserType;
import pie.service.StudentService.JoinGroupResult;
import pie.util.DatabaseConnector;

public class StaffService {

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
				String staffTitle = resultSet.getString("staffTitle");
				boolean staffIsSchoolAdmin = resultSet.getInt("staffIsSchoolAdmin") == 1;
				
				staff = new Staff(user, staffSchool, staffTitle, staffIsSchoolAdmin);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return staff;
	}
	
	public boolean isMember(int staffID, int groupID) {
		boolean isMember = false;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StaffGroup` WHERE staffID = ? AND groupID = ? AND staffGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return isMember;
		
	}
	
	public boolean setStaffRole(int staffID, int groupID, StaffRole staffRole) {
		boolean setResult = false;
		
		if (isMember(staffID, groupID)) {
			if (!getStaffRole(staffID, groupID).equals(staffRole)) {
				
				try {
					
					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					
					String sql = "UPDATE `StaffGroup` SET staffRoleID = ? WHERE staffID = ? AND groupID = ?";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, staffRole.getStaffRoleID());
					pst.setInt(2, staffID);
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
	
	public StaffRole getStaffRole(int staffID, int groupID) {
		
		StaffRoleService staffRoleService = new StaffRoleService();
		StaffRole staffRole = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffGroup` WHERE staffID = ? AND groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, groupID);
			resultSet = pst.executeQuery();
			
			if (resultSet.next()) {
				staffRole = staffRoleService.getStaffRole(resultSet.getInt(1));
			}

			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return staffRole;
	}

	public RegistrationResult registerStaff(String userFirstName,
			String userLastName, String userEmail, String userPassword,
			String userMobile, String schoolCode, String staffTitle) {
		
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

					String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile, userRegistrationDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";
					pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, UserType.STAFF.getUserTypeID());
					pst.setString(2, userFirstName);
					pst.setString(3, userLastName);
					pst.setString(4, userEmail);
					pst.setString(5, userPassword);
					pst.setString(6, userMobile);
					pst.executeUpdate();

					resultSet = pst.getGeneratedKeys();

					if (resultSet.next()) {

						int newUserID = resultSet.getInt(1);

						sql = "INSERT INTO `Staff` (staffID, schoolID, staffTitle) VALUES (?, ?, ?)";
						pst = conn.prepareStatement(sql);
						pst.setInt(1, newUserID);
						pst.setInt(2, schoolService.getSchoolID(schoolCode));
						pst.setString(3, staffTitle);
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

	public JoinGroupResult joinGroup(int groupID, int staffID, String staffRoleName) {
		JoinGroupResult joinGroupResult = JoinGroupResult.SUCCESS;

		GroupService groupService = new GroupService();
		StaffRoleService staffRoleService = new StaffRoleService();

		Group group = groupService.getGroup(groupID);
		
		if (group.groupIsOpen()) {
			if (group.groupIsValid()) {
				int staffRoleID = staffRoleService.getStaffRoleID(staffRoleName);
				groupService.addStaffToGroup(groupID, staffID, staffRoleID);
			} else {
				joinGroupResult = JoinGroupResult.GROUP_IS_NOT_VALID;
			}
		} else {
			joinGroupResult = JoinGroupResult.GROUP_IS_NOT_OPEN;
		}

		return joinGroupResult;
	}

}

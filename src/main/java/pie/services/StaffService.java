package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pie.Group;
import pie.School;
import pie.Staff;
import pie.StaffRole;
import pie.User;
import pie.UserType;
import pie.constants.JoinGroupResult;
import pie.constants.UserRegistrationResult;
import pie.utilities.DatabaseConnector;

public class StaffService {

	public Staff getStaff(int staffID) {

		SchoolService schoolService = new SchoolService();
		UserService userService = new UserService();

		Staff teacher = null;

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

				teacher = new Staff(user, staffSchool, staffTitle, staffIsSchoolAdmin);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacher;
	}
	
	public Group[] getJoinedGroups(int staffID) {
		
		GroupService groupService = new GroupService();
		Group[] joinedGroups = {};
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupID FROM `StaffGroup`,`Group` WHERE `StaffGroup`.groupID = `Group`.groupID AND groupIsValid = ? AND staffGroupIsValid = ? AND staffID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, staffID);

			resultSet = pst.executeQuery();

			List<Group> tempJoinedGroups = new ArrayList<Group>();
			while (resultSet.next()) {
				tempJoinedGroups.add(groupService.getGroup(resultSet.getInt(1)));
			}
			joinedGroups = tempJoinedGroups.toArray(joinedGroups);

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return joinedGroups;
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

	public UserRegistrationResult registerStaff(String userFirstName, String userLastName, String userEmail,
			String userPassword, String userMobile, String schoolCode, String staffTitle) {

		UserService userService = new UserService();
		SchoolService schoolService = new SchoolService();

		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else if (schoolService.isAvailableSchoolCode(schoolCode)){
			registrationResult = UserRegistrationResult.INVALID_SCHOOL_CODE;
		} else {
			
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
		}

		return registrationResult;
	}

	public JoinGroupResult joinGroup(int groupID, int staffID, String groupCode, StaffRole staffRole) {

		GroupService groupService = new GroupService();
		JoinGroupResult joinGroupResult = JoinGroupResult.SUCCESS;

		Group group = groupService.getGroup(groupID);
		
		if (group == null || !group.groupIsValid()) {
			joinGroupResult = JoinGroupResult.INVALID_GROUP;
		} else if (!group.groupIsOpen()) {
			joinGroupResult = JoinGroupResult.GROUP_CLOSED;
		} else if (group.getGroupCode() != null && groupCode == null) {
			joinGroupResult = JoinGroupResult.MISSING_GROUP_CODE;
		} else if (!group.getGroupCode().equals(groupCode)) {
			joinGroupResult = JoinGroupResult.INVALID_GROUP_CODE;
		} else {
			groupService.addStaffToGroup(groupID, staffID, staffRole);
		}

		return joinGroupResult;
	}

}

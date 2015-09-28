package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Group;
import pie.GroupType;
import pie.School;
import pie.Student;
import pie.Staff;
import pie.StaffRole;
import pie.util.DatabaseConnector;

public class GroupService {

	public enum RegistrationResult {
		SUCCESS("Group successfully registered."), NAME_TAKEN(
				"The group name you have entered is already taken!"), GROUP_CODE_TAKEN(
				"The group code you have entered is already taken!");

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

	public boolean isRegisteredGroup(String groupName) {
		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `Group` WHERE groupName = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupName);

			isRegistered = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isRegistered;
	}

	public boolean isAvailableGroupCode(String groupCode) {
		boolean isAvailable = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `Group` WHERE groupCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupCode);

			isAvailable = !pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isAvailable;
	}

	public int getGroupID(String groupCode) {
		int groupID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupID FROM `Group` WHERE groupCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupCode);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				groupID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return groupID;
	}

	public Group getGroup(int groupID) {
		Group group = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT * FROM `Group` WHERE groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				School groupSchool = new SchoolService().getSchool(resultSet
						.getInt("schoolID"));
				String groupName = resultSet.getString("groupName");
				String groupDescription = resultSet
						.getString("groupDescription");
				int groupMaxDailyHomeworkMinutes = resultSet
						.getInt("groupMaxDailyHomeworkMinutes");
				GroupType groupType = GroupType.getGroupType(resultSet
						.getInt("groupTypeID"));
				String groupCode = resultSet.getString("groupCode");
				boolean groupIsOpen = resultSet.getInt("groupCodeIsOpen") == 1;
				Date groupLastUpdate = new Date(resultSet.getTimestamp(
						"groupLastUpdate").getTime());
				Date groupDateCreated = new Date(resultSet.getTimestamp(
						"groupDateCreated").getTime());

				group = new Group(groupID, groupSchool, groupName,
						groupDescription, groupMaxDailyHomeworkMinutes,
						groupType, groupCode, groupIsOpen, groupLastUpdate,
						groupDateCreated);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return group;
	}

	public int getTotalHomeworkMinutesToday(int groupID) {

		int totalMinutes = 0;

		// Write codes to retrieve total minutes of homework for the day

		return totalMinutes;
	}

	public Staff getGroupOwner(int groupID) {

		StaffRoleService staffRoleService = new StaffRoleService();
		StaffService teacherService = new StaffService();

		Staff groupOwner = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffID FROM `Group`,`StaffGroup` WHERE `Group`.groupID = `StaffGroup`.groupID AND staffRoleID = ? AND `Group`.groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffRoleService.getOwnerStaffRole()
					.getStaffRoleID());
			pst.setInt(2, groupID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				groupOwner = teacherService.getStaff(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return groupOwner;
	}

	public boolean setGroupOwner(int groupID, int teacherID) {

		StaffService staffService = new StaffService();
		StaffRoleService teacherRoleService = new StaffRoleService();

		boolean setResult = false;
		
		if (getGroupOwner(groupID) != null) {
			StaffRole defaultTeacherRole = teacherRoleService.getDefaultStaffRole();
			staffService.setStaffRole(teacherID, groupID, defaultTeacherRole);
		}
		
		StaffRole ownerTeacherRole = teacherRoleService.getOwnerStaffRole();
		if (hasStaffMember(groupID, teacherID)) {
			if (staffService.setStaffRole(teacherID, groupID, ownerTeacherRole)) {
				setResult = true;
			}
		} else {
			if (addStaffToGroup(groupID, teacherID, ownerTeacherRole.getStaffRoleID())) {
				setResult = true;
			}
		}

		return setResult;
	}

	public boolean hasStudentMember(int groupID, int studentID) {
		boolean hasMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StudentGroup` WHERE studentID = ? AND groupID = ? AND studentGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			hasMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return hasMember;
	}

	public boolean hasStaffMember(int groupID, int staffID) {
		boolean hasMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StaffGroup` WHERE staffID = ? AND groupID = ? AND staffGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			hasMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return hasMember;
	}

	public boolean addStudentToGroup(int groupID, int studentID, int studentGroupIndexNumber) {

		boolean addResult = false;

		if (!hasStudentMember(groupID, studentID)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `StudentGroup` (groupID, studentID, studentGroupIndexNumber) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, studentID);
				pst.setInt(3, studentGroupIndexNumber);
				pst.executeQuery();

				addResult = true;

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return addResult;
	}

	public boolean addStaffToGroup(int groupID, int staffID, int staffRoleID) {

		boolean addResult = false;

		if (!hasStaffMember(groupID, staffID)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `StaffGroup` (groupID, staffID, staffRoleID) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, staffID);
				pst.setInt(3, staffRoleID);
				pst.executeQuery();

				addResult = true;

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return addResult;
	}

	public Staff[] getStaffMembers(int groupID) {

		Staff[] staffMembers = {};

		// Write codes to retrieve the Members of Group that are Teachers

		return staffMembers;
	}

	public Student[] getStudentMembers(int groupID) {
		Student[] studentMembers = {};

		// Write codes to retrieve the Members of Group that are Students

		return studentMembers;
	}

	public RegistrationResult registerGroup(Staff groupOwner,
			String groupName, String groupDescription,
			int groupMaxDailyHomeworkMinutes, GroupType groupType,
			String groupCode) {
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;

		if (isRegisteredGroup(groupName)) {
			registrationResult = RegistrationResult.NAME_TAKEN;
		} else {
			if (isAvailableGroupCode(groupCode)) {

				try {

					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					ResultSet resultSet = null;

					String sql = "INSERT INTO `Group` (schoolID, groupName, groupDescription, groupMaxDailyHomeworkMinutes, groupTypeID, groupCode) VALUES (?, ?, ?, ?, ?, ?)";
					pst = conn.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, groupOwner.getSchool().getSchoolID());
					pst.setString(2, groupName);
					pst.setString(3, groupDescription);
					pst.setInt(4, groupMaxDailyHomeworkMinutes);
					pst.setInt(5, groupType.getGroupTypeID());
					pst.setString(6, groupCode);
					pst.executeUpdate();

					resultSet = pst.getGeneratedKeys();

					if (resultSet.next()) {

						int newGroupID = resultSet.getInt(1);
						setGroupOwner(newGroupID, groupOwner.getUserID());
					}

					conn.close();

				} catch (Exception e) {

					System.out.println(e);
				}
			} else {
				registrationResult = RegistrationResult.GROUP_CODE_TAKEN;
			}
		}

		return registrationResult;
	}

}

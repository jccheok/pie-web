package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Group;
import pie.GroupType;
import pie.School;
import pie.Student;
import pie.Teacher;
import pie.util.DatabaseConnector;

public class GroupService {

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

	public Teacher getGroupOwner(int groupID) {

		TeacherRoleService teacherRoleService = new TeacherRoleService();
		TeacherService teacherService = new TeacherService();

		Teacher groupOwner = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherID FROM `Group`,`TeacherGroup` WHERE `Group`.groupID = `TeacherGroup`.groupID AND teacherRoleID = ? AND `Group`.groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherRoleService.getOwnerTeacherRole()
					.getTeacherRoleID());
			pst.setInt(2, groupID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				groupOwner = teacherService.getTeacher(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return groupOwner;
	}

	public boolean setGroupOwner(int groupID, int teacherID) {

		TeacherService teacherService = new TeacherService();
		TeacherRoleService teacherRoleService = new TeacherRoleService();

		boolean setResult = false;
		
		if (getGroupOwner(groupID) != null) {
			TeacherRole defaultTeacherRole = teacherRoleService.getDefaultTeacherRole();
			teacherService.setTeacherRole(teacherID, groupID, defaultTeacherRole);
		}
		
		TeacherRole ownerTeacherRole = teacherRoleService.getOwnerTeacherRole();
		if (hasTeacherMember(groupID, teacherID)) {
			if (teacherService.setTeacherRole(teacherID, groupID, ownerTeacherRole)) {
				setResult = true;
			}
		} else {
			if (addTeacherToGroup(groupID, teacherID, ownerTeacherRole.getTeacherRoleID())) {
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

	public boolean hasTeacherMember(int groupID, int teacherID) {
		boolean hasMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `TeacherGroup` WHERE teacherID = ? AND groupID = ? AND teacherGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);
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

	public boolean addTeacherToGroup(int groupID, int teacherID, int teacherRoleID) {

		boolean addResult = false;

		if (!hasTeacherMember(groupID, teacherID)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `TeacherGroup` (groupID, teacherID, teacherRoleID) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, teacherID);
				pst.setInt(3, teacherRoleID);
				pst.executeQuery();

				addResult = true;

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return addResult;
	}

	public Teacher[] getTeacherMembers(int groupID) {

		Teacher[] teacherMembers = {};

		// Write codes to retrieve the Members of Group that are Teachers

		return teacherMembers;
	}

	public Student[] getStudentMembers(int groupID) {
		Student[] studentMembers = {};

		// Write codes to retrieve the Members of Group that are Students

		return studentMembers;
	}

	public RegistrationResult registerGroup(Teacher groupOwner,
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

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
				group = new Group();
				group.setGroupID(groupID);
				group.setSchool(new SchoolService().getSchool(resultSet
						.getInt("schoolID")));
				group.setGroupDescription(resultSet
						.getString("groupDescription"));
				group.setGroupName(resultSet.getString("groupName"));
				group.setGroupMaxDailyHomeworkMinutes(resultSet
						.getInt("groupMaxDailyHomeworkMinutes"));
				group.setGroupType(GroupType.getGroupType(resultSet
						.getInt("groupTypeID")));
				group.setGroupCode(resultSet.getString("groupCode"));
				group.setGroupIsOpen(resultSet.getInt("groupIsOpen") == 1);
				group.setGroupLastUpdate(new Date(resultSet.getTimestamp(
						"groupLastUpdate").getTime()));
				group.setGroupDateCreated(new Date(resultSet.getTimestamp(
						"groupDateCreated").getTime()));
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

	public Teacher getAdministrator(int groupID) {

		Teacher administrator = null;

		// Write codes to retrieve Administrator of the group

		return administrator;
	}

	public boolean setAdministrator(int groupID) {

		boolean result = false;

		// Write codes to set the Administrator for the group

		return result;
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

	public boolean addStudentToGroup(int groupID) {
		boolean addResult = false;
	public RegistrationResult registerGroup(Teacher groupOwner,
			String groupName, String groupDescription,
			int groupMaxDailyHomeworkMinutes, GroupType groupType,
			String groupCode) {
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;

		// Add Student to Group
		if (isRegisteredGroup(groupName)) {
			registrationResult = RegistrationResult.NAME_TAKEN;
		} else {
			if (isAvailableGroupCode(groupCode)) {

		return addResult;
	}
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

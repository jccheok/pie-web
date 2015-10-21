package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Group;
import pie.GroupType;
import pie.School;
import pie.Staff;
import pie.Student;
import pie.constants.DeactivateGroupResult;
import pie.constants.GroupRegistrationResult;
import pie.utilities.DatabaseConnector;
import pie.utilities.Utilities;

public class GroupService {

	public boolean isRegisteredGroup(String groupName) {

		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `Group` WHERE name = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupName);

			isRegistered = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isRegistered;
	}

	public boolean isAvailableGroupCode(String groupCode) {

		boolean isAvailable = false;
		if(groupCode.equals("NONE")){
			isAvailable = true;
		}else{
			
			try {
	
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
	
				String sql = "SELECT * FROM `Group` WHERE code = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, groupCode);
	
				isAvailable = !pst.executeQuery().next();
	
				conn.close();
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isAvailable;
	}

	public int getGroupID(String groupCode) {

		int groupID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupID FROM `Group` WHERE code = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupCode);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				groupID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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

				School groupSchool = new SchoolService().getSchool(resultSet.getInt("schoolID"));
				String groupName = resultSet.getString("name");
				String groupDescription = resultSet.getString("description");
				int groupMaxDailyHomeworkMinutes = resultSet.getInt("maxDailyHomeworkMin");
				GroupType groupType = GroupType.getGroupType(resultSet.getInt("groupTypeID"));
				String groupCode = resultSet.getString("code");
				boolean groupIsValid = resultSet.getInt("isValid") == 1;
				boolean groupIsOpen = resultSet.getInt("isOpen") == 1;
				Date groupLastUpdate = new Date(resultSet.getTimestamp("lastUpdate").getTime());
				Date groupDateCreated = new Date(resultSet.getTimestamp("dateCreated").getTime());

				group = new Group(groupID, groupSchool, groupName, groupDescription, groupMaxDailyHomeworkMinutes,
						groupType, groupCode, groupIsValid, groupIsOpen, groupLastUpdate, groupDateCreated);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return group;
	}
	
	public boolean hasGroupMember(int groupID, int groupMemberID) {
		
		boolean hasMember = false;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT EXISTS (SELECT studentID from `StudentGroup` where studentID = ? AND groupID = ? AND isValid = ?) OR EXISTS (select staffID from `StaffGroup` where staffID = ? AND groupID = ? AND isValid = ?);";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupMemberID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);
			pst.setInt(4, groupMemberID);
			pst.setInt(5, groupID);
			pst.setInt(6, 1);
			resultSet = pst.executeQuery();
					
			if (resultSet.next()) {
				hasMember = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasMember;
	}



	public GroupRegistrationResult registerGroup(Staff groupOwner, String groupName, String groupDescription,
			int groupMaxDailyHomeworkMinutes, GroupType groupType, String groupCode, Date expiryDate, int subjectID) {

		GroupRegistrationResult registrationResult = GroupRegistrationResult.SUCCESS;
		
		StaffGroupService staffGroupService = new StaffGroupService();

		if (isRegisteredGroup(groupName)) {
			registrationResult = GroupRegistrationResult.NAME_TAKEN;
		} else {
			if (isAvailableGroupCode(groupCode)) {

				try {

					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					ResultSet resultSet = null;

					String sql = "INSERT INTO `Group` (schoolID, name, description, maxDailyHomeworkMin, groupTypeID, code, expiryDate, subjectID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, groupOwner.getSchool().getSchoolID());
					pst.setString(2, groupName);
					pst.setString(3, groupDescription);
					pst.setInt(4, groupMaxDailyHomeworkMinutes);
					pst.setInt(5, groupType.getGroupTypeID());
					pst.setString(6, groupCode);
					pst.setDate(7, new java.sql.Date(expiryDate.getTime()));
					pst.setInt(8, subjectID);
					pst.executeUpdate();

					resultSet = pst.getGeneratedKeys();

					if (resultSet.next()) {

						int newGroupID = resultSet.getInt(1);
						staffGroupService.setGroupOwner(newGroupID, groupOwner.getUserID());
					}

					conn.close();

				} catch (Exception e) {

					e.printStackTrace();
				}
			} else {
				registrationResult = GroupRegistrationResult.GROUP_CODE_TAKEN;
			}
		}

		return registrationResult;
	}

	public int getNextStudentIndexNumber(int groupID) {

		int nextStudentIndexNumber = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT COALESCE( (SELECT SUM(indexNumber) FROM `StudentGroup` WHERE groupID = ? ORDER BY indexNumber DESC LIMIT 1), 0) + 1";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				nextStudentIndexNumber = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextStudentIndexNumber;
	}

	public String generateGroupCode() {

		String newGroupCode = Utilities.generateString(5);
		while (!isAvailableGroupCode(newGroupCode)) {
			newGroupCode = Utilities.generateString(5);
		}

		return newGroupCode;
	}

	public boolean updateGroup(int groupID, String groupName, String groupDescription,
			int groupMaxDailyHomeworkMinutes, boolean groupIsOpen, String groupCode) {
		boolean updateResult = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Group` SET name = ?, description = ?, maxDailyHomeworkMin = ?, lastUpdate = NOW(), isOpen = ?, code = ? WHERE groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupName);
			pst.setString(2, groupDescription);
			pst.setInt(3, groupMaxDailyHomeworkMinutes);
			pst.setInt(4, groupID);
			pst.setString(5, groupCode);
			pst.setInt(6, groupIsOpen ? 1 : 0);

			pst.executeUpdate();

			updateResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return updateResult;
	}

	public int getMemberCount(int groupID) {
		int memberCount = 0;
		StudentGroupService studentGroupService = new StudentGroupService();
		StaffGroupService staffGroupService = new StaffGroupService();
		
		Student[] groupStudents = studentGroupService.getStudentMembers(groupID);
		Staff[] groupStaffs = staffGroupService.getStaffMembers(groupID);

		memberCount += groupStudents.length;
		memberCount += groupStaffs.length;

		return memberCount;
	}
	
	
	public boolean removeStudentFromGroup(int groupID, int studentID) {
		boolean removeResult = true;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `StudentGroup` SET isValid = ? WHERE groupID = ? AND studentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, groupID);
			pst.setInt(3, studentID);

			pst.executeUpdate();

			removeResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return removeResult;
	}

	public DeactivateGroupResult deactivateGroup(int groupID, int staffID, String userPassword) {
		DeactivateGroupResult deactivateGroupResult = DeactivateGroupResult.SUCCESS;
		
		StaffService staffService = new StaffService();
		StudentGroupService studentGroupService = new StudentGroupService();
		StaffGroupService staffGroupService = new StaffGroupService();
		
		Group group = getGroup(groupID);
		
		Staff staffUser = staffService.getStaff(staffID);
		Staff groupOwner = staffGroupService.getGroupOwner(groupID);
		
		if(!group.groupIsValid()){
			deactivateGroupResult = DeactivateGroupResult.GROUP_IS_NOT_VALID;
		}else if(staffID != groupOwner.getUserID()){
			deactivateGroupResult = DeactivateGroupResult.INVALID_USER;
		}else if(!staffUser.getUserPassword().equals(userPassword)){
			deactivateGroupResult = DeactivateGroupResult.WRONG_PASSWORD;
		}else{
			Student[] students = studentGroupService.getStudentMembers(groupID);
			Staff[] staffs = staffGroupService.getStaffMembers(groupID);

			for (Student student : students) {
				if (!removeStudentFromGroup(groupID, student.getUserID())) {
					deactivateGroupResult = DeactivateGroupResult.GENERAL_FAILURE;
					return deactivateGroupResult;
				}
			}

			for (Staff staff : staffs) {
				if (!staffGroupService.removeStaffFromGroup(groupID, staff.getUserID())) {
					deactivateGroupResult = DeactivateGroupResult.GENERAL_FAILURE;
					return deactivateGroupResult;
				}
			}

			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Group` SET isValid = ?, dateDeleted = NOW(), isOpen = ? WHERE groupID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 0);
				pst.setInt(2, 0);
				pst.setInt(3, groupID);

				pst.executeUpdate();
				
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return deactivateGroupResult;
	}
	
}

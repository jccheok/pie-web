package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pie.Group;
import pie.Staff;
import pie.StaffRole;
import pie.User;
import pie.UserType;
import pie.constants.JoinGroupResult;
import pie.constants.LeaveGroupResult;
import pie.constants.TransferGroupOwnershipResult;
import pie.utilities.DatabaseConnector;

public class StaffGroupService {

	
	public boolean addStaffToGroup(int groupID, int staffID, StaffRole staffRole) {

		boolean addResult = false;

		if (!isMember(staffID, groupID)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `StaffGroup` (groupID, staffID, staffRoleID) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, staffID);
				pst.setInt(3, staffRole.getStaffRoleID());
				pst.executeUpdate();

				addResult = true;

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return addResult;
	}
	
	public Staff[] getStaffMembers(int groupID) {

		StaffService staffService = new StaffService();
		Staff[] staffMembers = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffID FROM `StaffGroup` WHERE groupID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			ArrayList<Staff> tempStaffMembers = new ArrayList<Staff>();
			while (resultSet.next()) {
				tempStaffMembers.add(staffService.getStaff(resultSet.getInt(1)));
			}
			staffMembers = tempStaffMembers.toArray(staffMembers);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return staffMembers;
	}
	
	public Staff[] getGroupAdministrators(int groupID) {
		Staff[] groupAdmins = {};

		Staff[] groupStaff = getStaffMembers(groupID);
		StaffRoleService staffRoleService = new StaffRoleService();

		ArrayList<Staff> tempGroupAdmins = new ArrayList<Staff>();
		for (Staff staff : groupStaff) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "SELECT `StaffRole`.staffRoleID FROM `StaffRole`, `StaffGroup` WHERE `StaffRole`.staffRoleID = `StaffGroup`.staffRoleID AND `StaffGroup`.groupID = ? AND `StaffGroup`.staffID = ? AND `StaffGroup`.isValid = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, staff.getUserID());
				pst.setInt(3, 1);

				resultSet = pst.executeQuery();

				if (resultSet.next()) {
					int staffRoleID = resultSet.getInt(1);
					StaffRole staffRole = staffRoleService.getStaffRole(staffRoleID);

					if (staffRole.staffRoleIsAdmin()) {
						tempGroupAdmins.add(staff);
					}
				}

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		groupAdmins = tempGroupAdmins.toArray(groupAdmins);
		return groupAdmins;
	}

	public boolean removeStaffFromGroup(int groupID, int staffID) {
		boolean removeResult = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `StaffGroup` SET isValid = ? WHERE groupID = ? AND staffID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, groupID);
			pst.setInt(3, staffID);

			pst.executeUpdate();

			removeResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return removeResult;
	}
	
	public TransferGroupOwnershipResult transferGroupOwnership(int ownerID, int groupID, String transfereeEmail, String ownerPassword) {
		
		UserService userService = new UserService();
		TransferGroupOwnershipResult transferResult = TransferGroupOwnershipResult.SUCCESS;
		
		if (!userService.credentialsMatch(userService.getUser(ownerID).getUserEmail(), ownerPassword)) {
			transferResult = TransferGroupOwnershipResult.WRONG_PASSWORD;
		} else if (!userService.isRegisteredUser(transfereeEmail)) {
			transferResult = TransferGroupOwnershipResult.INVALID_TRANSFEREE;
		} else {
			
			User transfereeUser = userService.getUser(userService.getUserID(transfereeEmail));
			if (transfereeUser == null || transfereeUser.getUserType() != UserType.STAFF) {
				transferResult = TransferGroupOwnershipResult.INVALID_TRANSFEREE;
			} else {
				setGroupOwner(groupID, transfereeUser.getUserID());
				removeStaffFromGroup(groupID, ownerID);
			}
		}
		
		return transferResult;
	}

	public boolean setGroupOwner(int groupID, int staffID) {

		StaffRoleService staffRoleService = new StaffRoleService();
		GroupService groupService = new GroupService();

		boolean setResult = false;

		if (groupService.getGroupOwner(groupID) != null) {
			StaffRole defaultTeacherRole = staffRoleService.getDefaultStaffRole();
			setStaffRole(staffID, groupID, defaultTeacherRole);
		}

		StaffRole ownerTeacherRole = staffRoleService.getOwnerStaffRole();
		if (isMember(staffID, groupID)) {
			setResult = setStaffRole(staffID, groupID, ownerTeacherRole);
		} else {
			setResult = addStaffToGroup(groupID, staffID, ownerTeacherRole);
		}

		return setResult;
	}
	
	public Group[] getJoinedGroups(int staffID) {

		GroupService groupService = new GroupService();
		Group[] joinedGroups = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT `StaffGroup`.groupID FROM `StaffGroup`,`Group` WHERE `StaffGroup`.groupID = `Group`.groupID AND `Group`.isValid = ? AND `StaffGroup`.isValid = ? AND `StaffGroup`.staffID = ?";
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
			e.printStackTrace();
		}

		return joinedGroups;
	}
	
	public boolean isMember(int staffID, int groupID) {

		boolean isMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StaffGroup` WHERE staffID = ? AND groupID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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
					e.printStackTrace();
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

			String sql = "SELECT staffRoleID FROM `StaffGroup` WHERE staffID = ? AND groupID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				staffRole = staffRoleService.getStaffRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return staffRole;
	}
	
	public LeaveGroupResult leaveGroup(int groupID, int staffID) {
		LeaveGroupResult leaveGroupResult = LeaveGroupResult.SUCCESS;

		if (!isMember(staffID, groupID)) {
			leaveGroupResult = LeaveGroupResult.NOT_MEMBER;
		} else {
			removeStaffFromGroup(groupID, staffID);
		}

		return leaveGroupResult;
	}
	
	public Date getStaffGroupJoinedDate(int groupID, int userID) {

		Date userGroupJoinDate = null;
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT joinDate FROM `StaffGroup` WHERE groupID = ? AND staffID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			pst.setInt(2, userID);
			pst.setInt(3, 1);
			
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				userGroupJoinDate = new Date(resultSet.getTimestamp(1).getTime());
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userGroupJoinDate;
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
		} else if (groupService.hasGroupMember(groupID, staffID)) {
			joinGroupResult = JoinGroupResult.ALREADY_MEMBER;
		} else {
			addStaffToGroup(groupID, staffID, staffRole);
		}

		return joinGroupResult;
	}
}

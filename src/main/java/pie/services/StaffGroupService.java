package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import pie.Staff;
import pie.StaffRole;
import pie.User;
import pie.UserType;
import pie.constants.TransferGroupOwnershipResult;
import pie.utilities.DatabaseConnector;

public class StaffGroupService {

	
	public boolean addStaffToGroup(int groupID, int staffID, StaffRole staffRole) {

		StaffService staffService = new StaffService();
		boolean addResult = false;

		if (!staffService.isMember(staffID, groupID)) {

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

		StaffService staffService = new StaffService();
		StaffRoleService staffRoleService = new StaffRoleService();
		GroupService groupService = new GroupService();

		boolean setResult = false;

		if (groupService.getGroupOwner(groupID) != null) {
			StaffRole defaultTeacherRole = staffRoleService.getDefaultStaffRole();
			staffService.setStaffRole(staffID, groupID, defaultTeacherRole);
		}

		StaffRole ownerTeacherRole = staffRoleService.getOwnerStaffRole();
		if (staffService.isMember(staffID, groupID)) {
			setResult = staffService.setStaffRole(staffID, groupID, ownerTeacherRole);
		} else {
			setResult = addStaffToGroup(groupID, staffID, ownerTeacherRole);
		}

		return setResult;
	}
}

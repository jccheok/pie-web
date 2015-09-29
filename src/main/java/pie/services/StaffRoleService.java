package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pie.StaffRole;
import pie.constants.StaffRoleRegistrationResult;
import pie.utilities.DatabaseConnector;

public class StaffRoleService {

	public int getStaffRoleID(String staffRoleName) {
		
		int staffRoleID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE staffRoleName = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, staffRoleName);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				staffRoleID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return staffRoleID;
	}

	public StaffRole getStaffRole(int staffRoleID) {
		
		StaffRole staffRole = null;

		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
		
			String sql = "SELECT * FROM `StaffRole` WHERE staffRoleID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffRoleID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				staffRole = new StaffRole();
				staffRole.setStaffRoleID(staffRoleID);
				staffRole.setStaffRoleName(resultSet.getString("staffRoleName"));
				staffRole.setStaffRoleIsAdmin(resultSet.getInt("staffRoleIsAdmin") == 1);
				staffRole.setStaffRoleIsOwner(resultSet.getInt("staffRoleIsOwner") == 1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return staffRole;
	}

	public StaffRole[] getAllStaffRoles() {

		StaffRole[] staffRolesList = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<StaffRole> tempStaffRolesList = new ArrayList<StaffRole>();
			while (resultSet.next()) {
				tempStaffRolesList.add(getStaffRole(resultSet.getInt(1)));
			}
			staffRolesList = tempStaffRolesList.toArray(staffRolesList);

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return staffRolesList;
	}

	public StaffRole getAdminStaffRole() {
		
		StaffRole adminStaffRole = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE staffRoleIsAdmin = ? AND staffRoleIsOwner = ? AND staffRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 0);
			pst.setInt(3, 0);
			
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				adminStaffRole = getStaffRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return adminStaffRole;
	}

	public StaffRole getOwnerStaffRole() {
		
		StaffRole ownerStaffRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE staffRoleIsAdmin = ? AND staffRoleIsOwner = ? AND staffRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, 0);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				ownerStaffRole = getStaffRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return ownerStaffRole;
	}
	
	public StaffRole getDefaultStaffRole() {
		
		StaffRole defaultStaffRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE staffRoleIsAdmin = ? AND staffRoleIsOwner = ? AND staffRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 0);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				defaultStaffRole = getStaffRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return defaultStaffRole;
	}

	public StaffRoleRegistrationResult registerStaffRole(String staffRoleName, boolean staffRoleIsAdmin, boolean staffRoleIsOwner) {
		
		StaffRoleRegistrationResult registrationResult = StaffRoleRegistrationResult.SUCCESS;
		
		if (staffRoleIsOwner && getOwnerStaffRole() != null) {
			registrationResult = StaffRoleRegistrationResult.OWNER_EXISTS;
		} else if (getStaffRoleID(staffRoleName) > -1) {
			registrationResult = StaffRoleRegistrationResult.NAME_TAKEN;
		} else {

			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `StaffRole` (staffRoleName, staffRoleIsAdmin, staffRoleIsOwner) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, staffRoleName);
				pst.setInt(2, staffRoleIsAdmin? 1:0);
				pst.setInt(3, staffRoleIsOwner? 1:0);
				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return registrationResult;
	}
}

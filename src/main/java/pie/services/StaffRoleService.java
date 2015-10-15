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

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE name = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, staffRoleName);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				staffRoleID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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
				staffRole.setStaffRoleName(resultSet.getString("name"));
				staffRole.setStaffRoleIsAdmin(resultSet.getInt("isAdmin") == 1);
				staffRole.setStaffRoleIsOwner(resultSet.getInt("isOwner") == 1);
				staffRole.setStaffRoleIsDefault(resultSet.getInt("isDefault") == 1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}

		return staffRolesList;
	}

	public StaffRole getAdminStaffRole() {
		
		StaffRole adminStaffRole = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE isAdmin = ? AND isOwner = ? AND isDefault = ?";
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
			e.printStackTrace();
		}

		return adminStaffRole;
	}

	public StaffRole getOwnerStaffRole() {
		
		StaffRole ownerStaffRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE isAdmin = ? AND isOwner = ? AND isDefault = ?";
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
			e.printStackTrace();
		}
		
		return ownerStaffRole;
	}
	
	public StaffRole getDefaultStaffRole() {
		
		StaffRole defaultStaffRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffRoleID FROM `StaffRole` WHERE isAdmin = ? AND isOwner = ? AND isDefault = ?";
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
			e.printStackTrace();
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

				String sql = "INSERT INTO `StaffRole` (name, isAdmin, isOwner) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, staffRoleName);
				pst.setInt(2, staffRoleIsAdmin? 1:0);
				pst.setInt(3, staffRoleIsOwner? 1:0);
				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return registrationResult;
	}
	
	public boolean setStaffRoleOwner(int staffRoleID){
		boolean setOwnerResult = false;
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `StaffRole` SET isOwner = ?, isAdmin = ?, isDefault = ? WHERE staffRoleID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, 0);
			pst.setInt(4, staffRoleID);
			
			pst.executeUpdate();

			setOwnerResult = true;
			
			conn.close();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return setOwnerResult;
	}
	
	public boolean setStaffRoleAdmin(int staffRoleID){
		boolean setAdminResult = false;
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `StaffRole` SET isOwner = ?, isAdmin = ?, isDefault = ? WHERE staffRoleID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 1);
			pst.setInt(3, 0);
			pst.setInt(4, staffRoleID);
			
			pst.executeUpdate();

			setAdminResult = true;
			
			conn.close();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		return setAdminResult;
	}
}

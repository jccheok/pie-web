package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pie.TeacherRole;
import pie.util.DatabaseConnector;

public class TeacherRoleService {

	public enum RegistrationResult {
		SUCCESS("Teacher role registered."), NAME_TAKEN(
				"The teacher role name you have entered is already taken!"), OWNER_EXISTS(
				"An owner teacher role already exists!");

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

	public int getTeacherRoleID(String teacherRoleName) {
		int teacherRoleID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherRole` WHERE teacherRoleName = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, teacherRoleName);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				teacherRoleID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacherRoleID;
	}

	public TeacherRole getTeacherRole(int teacherRoleID) {
		TeacherRole teacherRole = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT * FROM `TeacherRole` WHERE teacherRoleID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherRoleID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				teacherRole = new TeacherRole();
				teacherRole.setTeacherRoleID(teacherRoleID);
				teacherRole.setTeacherRoleName(resultSet
						.getString("teacherRoleName"));
				teacherRole.setTeacherRoleIsAdmin(resultSet
						.getInt("teacherRoleIsAdmin") == 1);
				teacherRole.setTeacherRoleIsOwner(resultSet
						.getInt("teacherRoleIsOwner") == 1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacherRole;
	}

	public TeacherRole[] getAllTeacherRoles() {

		TeacherRole[] teacherRolesList = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherRole`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<TeacherRole> tempTeacherRolesList = new ArrayList<TeacherRole>();

			while (resultSet.next()) {
				tempTeacherRolesList.add(getTeacherRole(resultSet.getInt(1)));
			}

			teacherRolesList = tempTeacherRolesList.toArray(teacherRolesList);

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacherRolesList;
	}

	public TeacherRole getAdminTeacherRole() {
		TeacherRole adminTeacherRole = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherRole` WHERE teacherRoleIsAdmin = ? AND teacherRoleIsOwner = ? AND teacherRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 0);
			pst.setInt(3, 0);
			
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				adminTeacherRole = getTeacherRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return adminTeacherRole;
	}

	public TeacherRole getOwnerTeacherRole() {
		TeacherRole ownerTeacherRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherRole` WHERE teacherRoleIsAdmin = ? AND teacherRoleIsOwner = ? AND teacherRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, 0);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				ownerTeacherRole = getTeacherRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return ownerTeacherRole;
	}
	
	public TeacherRole getDefaultTeacherRole() {
		TeacherRole defaultTeacherRole = null;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherRole` WHERE teacherRoleIsAdmin = ? AND teacherRoleIsOwner = ? AND teacherRoleIsDefault = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 0);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				defaultTeacherRole = getTeacherRole(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return defaultTeacherRole;
	}

	public RegistrationResult registerTeacherRole(String teacherRoleName, boolean teacherRoleIsAdmin, boolean teacherRoleIsOwner) {
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;
		
		if (teacherRoleIsOwner) {
			if (getOwnerTeacherRole() != null) {
				// error
			} else {
				if (getTeacherRoleID(teacherRoleName) > -1) {
					registrationResult = RegistrationResult.NAME_TAKEN;
				} else {

					try {
						Connection conn = DatabaseConnector.getConnection();
						PreparedStatement pst = null;

						String sql = "INSERT INTO `TeacherRole` (teacherRoleName, teacherRoleIsAdmin, teacherRoleIsOwner) VALUES (?, ?, ?)";
						pst = conn.prepareStatement(sql);
						pst.setString(1, teacherRoleName);
						pst.setInt(2, teacherRoleIsAdmin? 1:0);
						pst.setInt(3, teacherRoleIsOwner? 1:0);
						pst.executeUpdate();

						conn.close();

					} catch (Exception e) {
						System.out.println(e);
					}

				}
			}
		}

		return registrationResult;
	}
}

package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pie.Group;
import pie.School;
import pie.Staff;
import pie.util.DatabaseConnector;

public class SchoolService {
	
	public enum RegistrationResult {
		SUCCESS("School successfully registered."), SCHOOL_CODE_TAKEN(
				"The school code you have entered is already taken!");

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

	public boolean isAvailableSchoolCode(String schoolCode) {
		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `School` WHERE schoolCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, schoolCode);

			isRegistered = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isRegistered;
	}

	public int getSchoolID(String schoolCode) {
		int schoolID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT schoolID FROM `School` WHERE schoolCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, schoolCode);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				schoolID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return schoolID;
	}

	public School getSchool(int schoolID) {

		School school = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT * FROM `School` WHERE schoolID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, schoolID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				school = new School();
				school.setSchoolID(schoolID);
				school.setSchoolAddress(new AddressService()
						.getAddress(resultSet.getInt("addressID")));
				school.setSchoolName(resultSet.getString("schoolName"));
				school.setSchoolCreatedDate(new Date(resultSet.getTimestamp(
						"schoolDateCreated").getTime()));
				school.setSchoolLastUpdate(new Date(resultSet.getTimestamp(
						"schoolLastUpdate").getTime()));
				school.setSchoolCode(resultSet.getString("schoolCode"));

			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return school;
	}

	public RegistrationResult registerSchool(String schoolName, String schoolCode) {

		RegistrationResult registrationResult = RegistrationResult.SUCCESS;

		if (isAvailableSchoolCode(schoolCode)) {
			
			try {
				
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				
				String sql = "INSERT INTO `School` (schoolname, schoolCode) VALUES (?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, schoolName);
				pst.setString(2, schoolCode);
				pst.executeUpdate();
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println(e);
			}
			
		} else {
			registrationResult = RegistrationResult.SCHOOL_CODE_TAKEN;
		}

		return registrationResult;
	}

	public School[] getAllSchools() {
		School[] schools = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT schoolID FROM `School`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<School> tempSchools = new ArrayList<School>();

			while (resultSet.next()) {

				tempSchools.add(getSchool(resultSet.getInt(1)));
			}

			schools = tempSchools
					.toArray(schools);
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return schools;
	}

	public Group[] getSchoolValidGroups(int schoolID) {
		GroupService groupService = new GroupService();

		Group[] schoolGroups = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupID FROM `Group` WHERE schoolID = ? AND groupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, schoolID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			List<Group> tempSchoolGroups = new ArrayList<Group>();

			while (resultSet.next()) {

				tempSchoolGroups.add(groupService
						.getGroup(resultSet.getInt(1)));
			}

			schoolGroups = tempSchoolGroups
					.toArray(schoolGroups);
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return schoolGroups;
	}

	public Staff[] getSchoolStaff(int schoolID) {
		Staff[] staff = {};

		// Write codes for retrieving all Teachers in School

		return staff;
	}

	public Staff[] getSchoolStaffAdministrators(int schoolID) {

		StaffService staffService = new StaffService();

		Staff[] schoolStaffAdministrators = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT staffID FROM `Staff` WHERE schoolID = ? AND staffIsSchoolAdmin = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, schoolID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			List<Staff> tempSchoolStaffAdministrators = new ArrayList<Staff>();

			while (resultSet.next()) {

				tempSchoolStaffAdministrators.add(staffService.getStaff(resultSet.getInt(1)));
			}

			schoolStaffAdministrators = tempSchoolStaffAdministrators
					.toArray(schoolStaffAdministrators);
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return schoolStaffAdministrators;
	}
}

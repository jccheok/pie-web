package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pie.Country;
import pie.Group;
import pie.School;
import pie.Teacher;
import pie.util.DatabaseConnector;

public class SchoolService {

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

		} catch (Exception e) {
			System.out.println(e);
		}

		return school;
	}

	public boolean registerSchool(String schoolName, String schoolPassword,
			Country schoolCountry, String schoolCity, String schoolAddress,
			String schoolPostalCode) {

		boolean registrationResult = false;

		try {
			
		} catch (Exception e) {
			
		}

		return registrationResult;
	}

	public School[] getAllSchools() {
		School[] schools = {};

		// Write codes for GetAllSchools

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

	public Teacher[] getSchoolTeachers(int schoolID) {
		Teacher[] teachers = {};

		// Write codes for retrieving all Teachers in School

		return teachers;
	}

	public Teacher[] getSchoolTeacherAdministrators(int schoolID) {

		TeacherService teacherService = new TeacherService();

		Teacher[] schoolTeacherAdministrators = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherID FROM `Teacher` WHERE schoolID = ? AND teacherIsSchoolAdmin = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, schoolID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			List<Teacher> tempSchoolTeachersAdministrators = new ArrayList<Teacher>();

			while (resultSet.next()) {

				tempSchoolTeachersAdministrators.add(teacherService
						.getTeacher(resultSet.getInt(1)));
			}

			schoolTeacherAdministrators = tempSchoolTeachersAdministrators
					.toArray(schoolTeacherAdministrators);
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return schoolTeacherAdministrators;
	}
}

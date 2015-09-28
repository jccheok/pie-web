package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Country;
import pie.Group;
import pie.School;
import pie.Teacher;
import pie.util.DatabaseConnector;

public class SchoolService {

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
	
	public boolean createSchool(String schoolName, String schoolPassword, Country schoolCountry, String schoolCity, String schoolAddress, String schoolPostalCode){
		
		boolean createdSchool = false;
		
		//Write codes for Create School
		
		return createdSchool;
	}
	
	public School[] getAllSchools(){
		School[] schools = {};
		
		//Write codes for GetAllSchools
		
		return schools;
	}
	
	public Group[] getSchoolGroups(int schoolID){
		Group[] groups = {};
		
		//Write codes for retrieving all Groups in the School
		
		return groups;
	}
	
	public Teacher[] getSchoolTeachers(int schoolID){
		Teacher[] teachers = {};
		
		//Write codes for retrieving all Teachers in School
		
		return teachers;
	}
	
	public Teacher[] getSchoolTeacherAdmins(int schoolID){
		Teacher[] admins = {};
		
		//Write codes for retrieving all Teachers that are Admins in the School
		
		return admins;
	}
}

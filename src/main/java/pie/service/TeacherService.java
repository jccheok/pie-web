package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.Teacher;
import pie.util.DatabaseConnector;

public class TeacherService {

	public Teacher getTeacher(int teacherID) {
		
		SchoolService schoolService = new SchoolService();
		UserService userService = new UserService();
		
		Teacher teacher = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Teacher` WHERE teacherID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				User user = userService.getUser(teacherID);
				School teacherSchool = schoolService.getSchool(resultSet.getInt("schoolID"));
				String teacherTitle = resultSet.getString("teacherTitle");
				boolean teacherIsSchoolAdmin = resultSet.getInt("teacherIsSchoolAdmin") == 1;
				
				teacher = new Teacher(user, teacherSchool, teacherTitle, teacherIsSchoolAdmin);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacher;
	}
	
	public boolean isMember(int teacherID, int groupID) {
		boolean isMember = false;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `TeacherGroup` WHERE teacherID = ? AND groupID = ? AND teacherGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return isMember;
		
	}
	
	public boolean setTeacherRole(int teacherID, int groupID, TeacherRole teacherRole) {
		boolean setResult = false;
		
		if (isMember(teacherID, groupID)) {
			if (!getTeacherRole(teacherID, groupID).equals(teacherRole)) {
				
				try {
					
					Connection conn = DatabaseConnector.getConnection();
					PreparedStatement pst = null;
					
					String sql = "UPDATE `TeacherGroup` SET teacherRoleID = ? WHERE teacherID = ? AND groupID = ?";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, teacherRole.getTeacherRoleID());
					pst.setInt(2, teacherID);
					pst.setInt(3, groupID);
					pst.executeUpdate();
					
					setResult = true;
					
					conn.close();
					
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		
		return setResult;
	}
	
	public TeacherRole getTeacherRole(int teacherID, int groupID) {
		
		TeacherRoleService teacherRoleService = new TeacherRoleService();
		TeacherRole teacherRole = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT teacherRoleID FROM `TeacherGroup` WHERE teacherID = ? AND groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, teacherID);
			pst.setInt(2, groupID);
			resultSet = pst.executeQuery();
			
			if (resultSet.next()) {
				teacherRole = teacherRoleService.getTeacherRole(resultSet.getInt(1));
			}

			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return teacherRole;
	}


}

package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.Teacher;
import pie.util.DatabaseConnector;

public class TeacherService {

	public Teacher getTeacher(int teacherID) {
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
				teacher = new Teacher();
				teacher.setUserID(resultSet.getInt("teacherID"));
				teacher.setTeacherTitle(resultSet.getString("teacherTitle"));
				teacher.setSchool(new SchoolService().getSchool(resultSet
						.getInt("schoolID")));
				teacher.setTeacherIsSchoolAdmin(resultSet
						.getInt("teacherIsSchoolAdmin") == 1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return teacher;
	}

}

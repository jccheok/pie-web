package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pie.Subject;
import pie.utilities.DatabaseConnector;

public class SubjectService {

	public Subject getSubject(int subjectID){
		Subject subject = null;
		
		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT * FROM `Subject` WHERE subjectID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, subjectID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				String subjectName = resultSet.getString("name");
				String abbreviation = resultSet.getString("abbreviation");

				subject = new Subject(subjectID, subjectName, abbreviation);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return subject;
	}
	
	public Subject[] getAllSubjects(){
		Subject[] subject = {};
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT subjectID FROM `Subject`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<Subject> tempSubjects = new ArrayList<Subject>();
			while (resultSet.next()) {
				tempSubjects.add(getSubject(resultSet.getInt("subjectID")));
			}
			subject = tempSubjects.toArray(subject);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return subject;
	}
}

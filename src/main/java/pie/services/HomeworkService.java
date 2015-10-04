package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Homework;
import pie.utilities.DatabaseConnector;

public class HomeworkService {

	public Homework getHomework(int homeworkID) {
		
		Homework homework = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Homework` WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				homework = new Homework();
				homework.setHomeworkID(homeworkID);
				homework.setHomeworkAuthor(new StaffService().getStaff(resultSet.getInt("teacherID")));
				homework.setHomeworkTitle(resultSet.getString("homworkTitle"));
				homework.setHomeworkDescription(resultSet.getString("homeworkDescription"));
				homework.setHomeworkMinutesRequired(resultSet.getInt("homeworkMinutesRequired"));
				homework.setHomeworkDueDate(new Date(resultSet.getTimestamp("homeworkDueDate").getTime()));
				homework.setHomeworkOpen(resultSet.getInt("homeworkIsOpen") == 1);
				homework.setHomeworkDateCreated(new Date(resultSet.getTimestamp("homeworkDateCreated").getTime()));
				homework.setHomeworkIsDraft(resultSet.getInt("homeworkIsDraft") == 1);
				homework.setHomeworkIsTemplate(resultSet.getInt("homeworkIsTemplate") == 1);
				homework.setHomeworkIsDeleted(resultSet.getInt("homeworkIsDeleted") == 1);
				homework.setHomeworkDateDeleted(new Date(resultSet.getTimestamp("homeworkDateDeleted").getTime()));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}
}

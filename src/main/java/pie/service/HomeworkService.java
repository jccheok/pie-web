package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Group;
import pie.Homework;
import pie.Staff;
import pie.User;
import pie.util.DatabaseConnector;

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
				homework.setHomeworkAuthor(new StaffService().getStaff(resultSet
						.getInt("staffID")));
				homework.setHomeworkTitle(resultSet.getString("homworkTitle"));
				homework.setHomeworkDescription(resultSet
						.getString("homeworkDescription"));
				homework.setHomeworkMinutesRequired(resultSet
						.getInt("homeworkMinutesRequired"));
				homework.setHomeworkDueDate(new Date(resultSet.getTimestamp(
						"homeworkDueDate").getTime()));
				homework.setHomeworkOpen(resultSet.getInt("homeworkIsOpen") == 1);
				homework.setHomeworkDateCreated(new Date(resultSet
						.getTimestamp("homeworkDateCreated").getTime()));
				homework.setHomeworkIsDraft(resultSet.getInt("homeworkIsDraft") == 1);
				homework.setHomeworkIsTemplate(resultSet
						.getInt("homeworkIsTemplate") == 1);
				homework.setHomeworkIsDeleted(resultSet
						.getInt("homeworkIsDeleted") == 1);
				homework.setHomeworkDateDeleted(new Date(resultSet
						.getTimestamp("homeworkDateDeleted").getTime()));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return homework;
	}

	public Group[] getGroupRecipient(int homeworkID) {
		Group[] groupRecipient = {};

		// Write codes to retrieve Group Recipients of Event

		return groupRecipient;
	}

	public User[] getUserRecipient(int homeworkID) {
		User[] userRecipient = {};

		// Write codes to retrieve User Recipients of Event

		return userRecipient;
	}

	public int createHomework(String homeworkTitle, Staff staff, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired,
			Date homeworkDueDate, boolean homeworkIsOpen) {
		int homeworkID = -1;

		// Write codes to send Homework

		return homeworkID;
	}
	
	public int createHomeworkAsDraft (String homeworkTitle, Staff staff, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired,
			Date homeworkDueDate, boolean homeworkIsOpen){
		int homeworkID = -1;
		int homeworkIsDraft = 1;
		
		//Write codes to set Homework as Draft
		
		return homeworkID;
	}
	
	public int createHomeworkAsTemplate(String homeworkTitle, Staff staff, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired,
			Date homeworkDueDate, boolean homeworkIsOpen){
		int homeworkID = -1;
		int homeworkIsTemplate = 1;
		
		// Write codes to set Homework as Template
		
		return homeworkID;
	}
	
	public boolean deleteHomework(int homeworkID){
		boolean deleteResult = false;
		
		//Write codes to set Homework as Delete
		
		return deleteResult;
	}
	
	public boolean sendHomeworkDraft(int homeworkID){
		boolean sendResult = false;
		int homeworkIsDraft = 0;
		
		//Write codes to send Homework and set HomeworkIsDraft to false
		
		return sendResult;
	}

	public Homework[] getHomeworksIsDraft(Staff staff){
		Homework[] homework = {};
		
		//Write codes to retrieve Homework that are drafted by Staff
		
		return homework;
	}
	
	public Homework[] getHomeworkIsTemplate(Staff staff){
		Homework[] homework = {};
		
		// Write codes to retrieve Homework Templates that are saved by Staff
		
		return homework;
	}
}

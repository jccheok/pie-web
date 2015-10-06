package pie.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import pie.Homework;
import pie.Student;
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

	public boolean createHomework(int staffID, int groupID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate) {

		boolean createResult = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO  `Homework` (staffID ,homeworkTitle ,homeworkSubject ,homeworkDescription ,homeworkMinutesRequired "
					+ ",`homeworkDueDate`)VALUES" + "(?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setString(2, homeworkTitle);
			pst.setString(3, homeworkSubject);
			pst.setString(4, homeworkDescription);
			pst.setInt(5, homeworkMinutesRequired);
			pst.setDate(6, homeworkDueDate);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				int homeworkID = resultSet.getInt(1);
				sql = "INSERT GroupHomework(staffID,groupID,homeworkID) VALUES(?,?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, staffID);
				pst.setInt(2, groupID);
				pst.setInt(3, homeworkID);

				pst.executeUpdate();
				createResult = true;
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return createResult;
	}

	public boolean openHomework(int homeworkID) {

		boolean success = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "UPDATE Homework SET `homeworkIsDraft` = ? , `homeworkIsOpen` = ? WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 0);
			pst.setInt(3, homeworkID);
			pst.executeUpdate();
			success = true;
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public Homework[] getAllDraftHomework(int staffID) {

		Homework[] homework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkID FROM Homework WHERE staffID = ? AND homeworkIsDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempHomework.add(getHomework(resultSet.getInt(1)));
			}

			homework = tempHomework.toArray(homework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;

	}

	public Homework[] getSentHomework(int staffID) {
		Homework[] homework = {};
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkID FROM Homework WHERE staffID = ? AND homeworkIsDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 0);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempHomework.add(getHomework(resultSet.getInt(1)));
			}

			homework = tempHomework.toArray(homework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public boolean updateDraftHomework(int homeworkID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate) {

		boolean isUpdated = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "UPDATE Homework SET homeworkTitle = ?, homeworkSubject = ?, homeworkDescription = ?, int homeworkMinutesRequired, Date homeworkDueDate WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, homeworkTitle);
			pst.setString(2, homeworkSubject);
			pst.setString(3, homeworkDescription);
			pst.setInt(4, homeworkMinutesRequired);
			pst.setDate(5, homeworkDueDate);
			pst.setInt(6, homeworkID);

			if (pst.executeUpdate() != 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	public boolean sendHomework(int groupID, int homeworkID) {

		boolean sendSuccess = false;

		GroupService gs = new GroupService();
		Student[] groupStudents = gs.getStudentMembers(groupID);

		for (Student student : groupStudents) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT `UserHomework`(homeworkID,userID)VALUES(?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, homeworkID);
				pst.setInt(2, student.getUserID());

				if (pst.executeUpdate() != 0) {
					sendSuccess = true;
				} else {
					sendSuccess = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sendSuccess;
	}

	public boolean publishHomework(int groupID, int homeworkID) {

		boolean publishResult = false;

		if (openHomework(homeworkID)) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "UPDATE `GroupHomework` SET groupHomeworkPublishDate = NOW() WHERE homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, homeworkID);

				if (pst.executeUpdate() != 0) {
					if (sendHomework(groupID, homeworkID)) {
						publishResult = true;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return publishResult;
	}

}

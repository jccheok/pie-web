package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import pie.Homework;
import pie.Staff;
import pie.constants.DeleteHomeworkResult;
import pie.constants.PublishHomeworkResult;
import pie.constants.UpdateHomeworkDraftResult;
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
				StaffService staffService = new StaffService();

				Staff author = staffService.getStaff(resultSet.getInt("authorID"));
				String title = resultSet.getString("title");
				String subject = resultSet.getString("subject");
				String description = resultSet.getString("description");
				Date dateCreated = new Date(resultSet.getDate("dateCreated").getTime());
				int minutesReqStudent = resultSet.getInt("minutesReqPerStudent");
				boolean isDraft = resultSet.getInt("isDraft") == 1 ? true : false;
				boolean isDeleted = resultSet.getInt("isDeleted") == 1 ? true : false;
				String level = resultSet.getString("level");

				homework = new Homework(homeworkID, author, title, subject, description, minutesReqStudent, dateCreated,
						isDraft, isDeleted, level);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public int publishHomework(int authorID, String title, String subject, String description, int minutesRequired,
			String level) {

		int homeworkID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (authorID,title,subject,description,minutesReqPerStudent,level,isDraft,dateCreated) VALUES(?,?,?,?,?,?,?,NOW())";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, authorID);
			pst.setString(2, title);
			pst.setString(3, subject);
			pst.setString(4, description);
			pst.setInt(5, minutesRequired);
			pst.setString(6, level);
			pst.setInt(7, 0);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				homeworkID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkID;
	}

	public PublishHomeworkResult publishDraftHomework(Homework homework) {

		PublishHomeworkResult result = null;

		if (updateDraftHomework(homework).equals(UpdateHomeworkDraftResult.SUCCESS)) {

			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET isDraft = ? WHERE homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 0);
				pst.setInt(2, homework.getHomeworkID());

				if (pst.executeUpdate() == 0) {
					result = PublishHomeworkResult.FAILED_DRAFT;
				} else {
					result = PublishHomeworkResult.SUCCESS;
				}

				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = PublishHomeworkResult.FAILED_TO_UPDATE_HOMEWORK;
		}

		return result;
	}

	public int saveHomeworkAsDraft(Homework homework) {

		int homeworkID = -1;
		if (homework.getHomeworkDescription() == null || homework.getHomeworkDescription().length() == 0) {
			homework.setHomeworkDescription("No Description");
		}

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (authorID,title,subject,description,minutesReqPerStudent, "
					+ "level,dateCreated) VALUES (?,?,?,?,?,?,NOW())";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, homework.getHomeworkAuthor().getUserID());
			pst.setString(2, homework.getHomeworkTitle());
			pst.setString(3, homework.getHomeworkSubject());
			pst.setString(4, homework.getHomeworkDescription());
			pst.setInt(5, homework.gethomeworkMinutesReqStudent());
			pst.setString(6, homework.getHomeworkLevel());
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				homeworkID = resultSet.getInt(1);
			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkID;
	}

	public Homework[] getAllDraftHomework(int authorID) {

		Homework[] homework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkID FROM `Homework` WHERE authorID = ? AND isDraft = ? AND isDeleted = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, authorID);
			pst.setInt(2, 1);
			pst.setInt(3, 0);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempDraftHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempDraftHomework.add(getHomework(resultSet.getInt("homeworkID")));
			}

			homework = tempDraftHomework.toArray(homework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;

	}

	public Homework[] getAllPublishedHomework() {
		Homework[] homework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Homework` WHERE isDraft = ? AND isDeleted = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 0);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempDraftHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempDraftHomework.add(getHomework(resultSet.getInt("homeworkID")));
			}

			homework = tempDraftHomework.toArray(homework);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public DeleteHomeworkResult deleteHomework(Homework homework) {
		DeleteHomeworkResult result = DeleteHomeworkResult.SUCCESS;

		if (!homework.isHomeworkIsDraft()) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET isDeleted = ?,dateDeleted = NOW() WHERE homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 1);
				pst.setInt(2, homework.getHomeworkID());

				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				result = DeleteHomeworkResult.FAILED_TO_SET_TO_DELETE;
			}
		} else if (homework.isHomeworkIsDraft()) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "DELETE FROM `Homework` WHERE homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, homework.getHomeworkID());

				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				result = DeleteHomeworkResult.FAILED_TO_SET_TO_DELETE;
			}
		}

		return result;
	}

	public UpdateHomeworkDraftResult updateDraftHomework(Homework homework) {

		UpdateHomeworkDraftResult updateHomeworkDraftResult = UpdateHomeworkDraftResult.SUCCESS;

		if (homework.isHomeworkIsDraft()) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET title = ?, subject = ?, description = ?, "
						+ "minutesReqPerStudent = ?, level = ? WHERE homeworkID = ?";

				pst = conn.prepareStatement(sql);
				pst.setString(1, homework.getHomeworkTitle());
				pst.setString(2, homework.getHomeworkSubject());
				pst.setString(3, homework.getHomeworkDescription());
				pst.setInt(4, homework.gethomeworkMinutesReqStudent());
				pst.setString(5, homework.getHomeworkLevel());
				pst.setInt(6, homework.getHomeworkID());

				if (pst.executeUpdate() == 0) {
					updateHomeworkDraftResult = UpdateHomeworkDraftResult.FAIL_TO_UPDATE_HOMEWORK;
				}

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			updateHomeworkDraftResult = UpdateHomeworkDraftResult.HOMEWORK_IS_NOT_DRAFT;
		}

		return updateHomeworkDraftResult;
	}
}

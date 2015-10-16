package pie.services;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import pie.Homework;
import pie.Staff;
import pie.Student;
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
				Date dateDeleted = new Date(resultSet.getDate("dateDeleted").getTime());
				String level = resultSet.getString("level");

				homework = new Homework(homeworkID, author, title, subject, description, minutesReqStudent, dateCreated,
						isDraft, isDeleted, dateDeleted, level);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public int createHomework(int authorID, String title, String subject, String description, int minutesReqPerStudent,
			String level) {

		int homeworkID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (authorID,title,subject,description,minutesReqPerStudent, "
					+ "level,isDraft,dateCreated) VALUES (?,?,?,?,?,?,?,NOW())";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, authorID);
			pst.setString(2, title);
			pst.setString(3, subject);
			pst.setString(4, description);
			pst.setInt(5, minutesReqPerStudent);
			pst.setString(6, level);
			pst.setInt(7, 0);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			homeworkID = resultSet.getInt(1);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkID;
	}

	public int createHomeworkAsDraft(int authorID, String title, String subject, String description,
			int minutesReqPerStudent, String level) {

		int homeworkID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (authorID,title,subject,description,minutesReqPerStudent, "
					+ "level,dateCreated) VALUES (?,?,?,?,?,?,NOW())";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, authorID);
			pst.setString(2, title);
			pst.setString(3, subject);
			pst.setString(4, description);
			pst.setInt(5, minutesReqPerStudent);
			pst.setString(6, level);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			homeworkID = resultSet.getInt(1);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkID;
	}

	public Homework[] getAllDraftHomework(int staffID) {

		Homework[] homework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkID FROM `Homework` WHERE authorID = ? AND isDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 1);

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

	public Homework[] getPublishDraftHomework(int staffID) {
		Homework[] homework = {};
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `GroupHomework` WHERE publisherID = ? AND isDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempDraftHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempDraftHomework.add(getHomework(resultSet.getInt("homeworkID")));
			}

			homework = tempDraftHomework.toArray(homework);

			conn.close();
		} catch (Exception e) {

		}

		return null;
	}

	public Homework[] getSentHomework(int staffID) {
		Homework[] homework = {};
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkID FROM `Homework` WHERE publisherID = ? AND IsDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 0);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempSentHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempSentHomework.add(getHomework(resultSet.getInt(1)));
			}

			homework = tempSentHomework.toArray(homework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public boolean deleteDraftHomework(int homeworkID) {

		boolean deleteResult = false;

		if (getHomework(homeworkID).isHomeworkIsDraft()) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "DELETE FROM `Homework`, `GroupHomework` WHERE `Homework`.homeworkID = `GroupHomework`.homeworkID AND `Homework`.homeworkID = ? AND homeworkIsDraft = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, homeworkID);
				pst.setInt(2, 1);

				pst.executeUpdate();

				deleteResult = true;

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return deleteResult;

	}

	public boolean deleteHomework(int homeworkID) {
		boolean deleteResult = false;

		if (!getHomework(homeworkID).isHomeworkIsDraft()) {

			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET homeworkIsDeleted = ? WHERE homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 1);
				pst.setInt(2, homeworkID);

				pst.executeUpdate();

				deleteResult = true;

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return deleteResult;
	}

	public UpdateHomeworkDraftResult updateDraftHomework(int homeworkID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate, boolean homeworkIsGraded) {

		UpdateHomeworkDraftResult updateHomeworkDraftResult = UpdateHomeworkDraftResult.SUCCESS;

		if (getHomework(homeworkID).isHomeworkIsDraft()) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET homeworkTitle = ?, homeworkSubject = ?, homeworkDescription = ?, "
						+ "homeworkMinutesRequired = ?, homeworkDueDate = ?, homeworkIsGraded = ? WHERE homeworkID = ?";

				pst = conn.prepareStatement(sql);
				pst.setString(1, homeworkTitle);
				pst.setString(2, homeworkSubject);
				pst.setString(3, homeworkDescription);
				pst.setInt(4, homeworkMinutesRequired);
				pst.setDate(5, new java.sql.Date(homeworkDueDate.getTime()));
				pst.setInt(6, homeworkIsGraded ? 1 : 0);
				pst.setInt(7, homeworkID);

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

	public boolean sendHomework(int studentID, int homeworkID) {

		boolean sendResult = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "INSERT INTO `UserHomework` (homeworkID, userID) VALUES (?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);
			pst.setInt(2, studentID);

			pst.executeUpdate();

			sendResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sendResult;
	}

	public PublishHomeworkResult publishHomework(int groupID, int homeworkID, int publisherID) {

		PublishHomeworkResult publishResult = PublishHomeworkResult.SUCCESS;
		GroupService groupService = new GroupService();

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Homework` SET homeworkIsDraft = ? , homeworkIsOpen = ? WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 1);
			pst.setInt(3, homeworkID);

			if (pst.executeUpdate() == 0) {
				publishResult = PublishHomeworkResult.FAILED_DRAFT;
			} else {
				sql = "INSERT INTO `GroupHomework` (groupHomeworkPublishDate, groupID, homeworkID, staffID) VALUES(NOW(), ?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, homeworkID);
				pst.setInt(3, publisherID);

				if (pst.executeUpdate() == 0) {
					publishResult = PublishHomeworkResult.FAILED_TO_UPDATE_GROUP;
				} else {
//					Student[] groupStudents = groupService.getStudentMembers(groupID);
//
//					for (Student student : groupStudents) {
//						if (!sendHomework(student.getUserID(), homeworkID)) {
//							publishResult = PublishHomeworkResult.FAILED_TO_SEND_TO_MEMBERS;
//						}
//					}
					System.out.print("banana");
				}
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publishResult;
	}

	public Homework[] getAllHomework(int groupID) {

		Homework[] listHomeworks = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT `Homework`.homeworkID FROM `GroupHomework` WHERE groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			resultSet = pst.executeQuery();

			ArrayList<Homework> tempHwList = new ArrayList<Homework>();

			while (resultSet.next()) {
				tempHwList.add(getHomework(resultSet.getInt(1)));
			}

			tempHwList.toArray(listHomeworks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listHomeworks;
	}

	public boolean isAuthor(int staffID, int homeworkID) {
		boolean isAuthor = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Homework` WHERE staffID = ? AND homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, homeworkID);
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isAuthor = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAuthor;
	}

}

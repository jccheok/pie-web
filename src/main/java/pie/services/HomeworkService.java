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

				Staff homeworkAuthor = new StaffService().getStaff(resultSet.getInt("staffID"));
				String homeworkTitle = resultSet.getString("homeworkTitle");
				String homeworkSubject = resultSet.getString("homeworkSubject");
				String homeworkDescription = resultSet.getString("homeworkDescription");
				int homeworkMinutesRequired = resultSet.getInt("homeworkMinutesRequired");
				Date homeworkDueDate = new Date(resultSet.getTimestamp("homeworkDueDate").getTime());
				Date homeworkDateCreated = new Date(resultSet.getTimestamp("homeworkDateCreated").getTime());
				boolean homeworkIsOpen = resultSet.getInt("homeworkIsOpen") == 1 ? true : false;
				boolean homeworkIsDraft = resultSet.getInt("homeworkIsDraft") == 1 ? true : false;
				boolean homeworkIsTemplate = resultSet.getInt("homeworkIsTemplate") == 1 ? true : false;
				boolean homeworkIsDeleted = resultSet.getInt("homeworkIsDeleted") == 1 ? true : false;
				Date homeworkDateDeleted = new Date(resultSet.getTimestamp("homeworkDateDeleted").getTime());
				boolean homeworkIsGraded = resultSet.getInt("homeworkIsGraded") == 1 ? true : false;

				homework = new Homework(homeworkID, homeworkAuthor, homeworkTitle, homeworkSubject, homeworkDescription,
						homeworkMinutesRequired, homeworkDueDate, homeworkIsOpen, homeworkDateCreated, homeworkIsDraft,
						homeworkIsTemplate, homeworkIsDeleted, homeworkDateDeleted, homeworkIsGraded);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public int createHomework(int staffID, int groupID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesReqStudent, Date homeworkDueDate, boolean homeworkIsGraded) {

		int homeworkID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (staffID ,homeworkTitle ,homeworkSubject ,homeworkDescription ,homeworkMinutesReqStudent "
					+ ",homeworkDueDate,homeworkIsGraded) VALUES (?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, staffID);
			pst.setString(2, homeworkTitle);
			pst.setString(3, homeworkSubject);
			pst.setString(4, homeworkDescription);
			pst.setInt(5, homeworkMinutesReqStudent);
			pst.setDate(6, new java.sql.Date(homeworkDueDate.getTime()));
			pst.setInt(7, homeworkIsGraded ? 1 : 0);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				homeworkID = resultSet.getInt(1);
				sql = "INSERT INTO `GroupHomework`(staffID,groupID,homeworkID) VALUES(?,?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, staffID);
				pst.setInt(2, groupID);
				pst.setInt(3, homeworkID);

				pst.executeUpdate();
			}
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

			String sql = "SELECT homeworkID FROM `Homework` WHERE staffID = ? AND homeworkIsDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			ArrayList<Homework> tempDraftHomework = new ArrayList<Homework>();
			while (resultSet.next()) {
				tempDraftHomework.add(getDraftHomework(resultSet.getInt("homeworkID")));
			}

			homework = tempDraftHomework.toArray(homework);

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

		if (!isDraftHomework(homeworkID)) {

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

		if (isDraftHomework(homeworkID)) {
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

	public PublishHomeworkResult publishHomework(int groupID, int homeworkID, int staffID) {

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
				pst.setInt(3, staffID);

				if (pst.executeUpdate() == 0) {
					publishResult = PublishHomeworkResult.FAILED_TO_UPDATE_GROUP;
				} else {
					Student[] groupStudents = groupService.getStudentMembers(groupID);

					for (Student student : groupStudents) {
						if (!sendHomework(student.getUserID(), homeworkID)) {
							publishResult = PublishHomeworkResult.FAILED_TO_SEND_TO_MEMBERS;
						}
					}
				}
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publishResult;
	}

	public Homework getDraftHomework(int homeworkID) {

		Homework homework = null;

		Staff homeworkAuthor = null;
		String homeworkTitle = null;
		String homeworkSubject = null;
		String homeworkDescription = null;
		int homeworkMinutesRequired = 0;
		Date homeworkDueDate = null;
		Date homeworkDateCreated = null;
		boolean homeworkIsOpen = false;
		boolean homeworkIsDraft = false;
		boolean homeworkIsTemplate = false;
		boolean homeworkIsDeleted = false;
		Date homeworkDateDeleted = null;
		boolean homeworkIsGraded = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Homework` WHERE homeworkID = ? AND homeworkIsDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				homeworkAuthor = new StaffService().getStaff(resultSet.getInt("staffID"));
				homeworkTitle = resultSet.getString("homeworkTitle");
				homeworkSubject = resultSet.getString("homeworkSubject");
				homeworkDescription = resultSet.getString("homeworkDescription");
				homeworkMinutesRequired = resultSet.getInt("homeworkMinutesRequired");
				homeworkDueDate = new Date(resultSet.getTimestamp("homeworkDueDate").getTime());
				homeworkDateCreated = new Date(resultSet.getTimestamp("homeworkDateCreated").getTime());
				homeworkIsOpen = resultSet.getInt("homeworkIsOpen") == 1 ? true : false;
				homeworkIsDraft = resultSet.getInt("homeworkIsDraft") == 1 ? true : false;
				homeworkIsTemplate = resultSet.getInt("homeworkIsTemplate") == 1 ? true : false;
				homeworkIsDeleted = resultSet.getInt("homeworkIsDeleted") == 1 ? true : false;
				homeworkDateDeleted = new Date(resultSet.getTimestamp("homeworkDateDeleted").getTime());
				homeworkIsGraded = resultSet.getInt("homeworkIsGraded") == 1 ? true : false;

				homework = new Homework(homeworkID, homeworkAuthor, homeworkTitle, homeworkSubject, homeworkDescription,
						homeworkMinutesRequired, homeworkDueDate, homeworkIsOpen, homeworkDateCreated, homeworkIsDraft,
						homeworkIsTemplate, homeworkIsDeleted, homeworkDateDeleted, homeworkIsGraded);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
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

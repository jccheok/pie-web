package pie.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import pie.Homework;
import pie.Staff;
import pie.Student;
import pie.constants.PublishHomeworkResult;
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

				Staff homeworkAuthor = new StaffService().getStaff(resultSet.getInt("teacherID"));
				String homeworkTitle = resultSet.getString("homworkTitle");
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

				homework = new Homework(homeworkID, homeworkAuthor, homeworkTitle, homeworkSubject,
						homeworkDescription,
						homeworkMinutesRequired, homeworkDueDate, homeworkIsOpen, homeworkDateCreated, homeworkIsDraft,
						homeworkIsTemplate, homeworkIsDeleted, homeworkDateDeleted);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

	public int createHomework(int staffID, int groupID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate) {

		int homeworkID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Homework` (staffID ,homeworkTitle ,homeworkSubject ,homeworkDescription ,homeworkMinutesRequired "
					+ ",homeworkDueDate) VALUES (?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, staffID);
			pst.setString(2, homeworkTitle);
			pst.setString(3, homeworkSubject);
			pst.setString(4, homeworkDescription);
			pst.setInt(5, homeworkMinutesRequired);
			pst.setDate(6, homeworkDueDate);
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

			String sql = "SELECT homeworkID FROM `Homework` WHERE staffID = ? AND homeworkIsDraft = ?";
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

	public boolean isDraftHomework(int homeworkID) {

		boolean isDraft = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT homeworkIsDraft FROM `Homework` WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				isDraft = resultSet.getInt("homeworkIsDraft") == 1 ? true : false;
			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isDraft;
	}

	public boolean deleteDraftHomework(int homeworkID) {

		boolean deleteResult = false;

		if (isDraftHomework(homeworkID)) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "DELETE FROM `Homework` WHERE homeworkID = ? AND homeworkIsDraft = ?";
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

	public boolean updateDraftHomework(int homeworkID, String homeworkTitle, String homeworkSubject,
			String homeworkDescription, int homeworkMinutesRequired, Date homeworkDueDate) {

		boolean isUpdated = false;
		if (isDraftHomework(homeworkID)) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `Homework` SET homeworkTitle = ?, homeworkSubject = ?, homeworkDescription = ?, "
						+ "homeworkMinutesRequired = ?, homeworkDueDate = ? WHERE homeworkID = ?";

				pst = conn.prepareStatement(sql);
				pst.setString(1, homeworkTitle);
				pst.setString(2, homeworkSubject);
				pst.setString(3, homeworkDescription);
				pst.setInt(4, homeworkMinutesRequired);
				pst.setDate(5, homeworkDueDate);
				pst.setInt(6, homeworkID);

				pst.executeUpdate();

				isUpdated = true;

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isUpdated;
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

	public PublishHomeworkResult publishHomework(int groupID, int homeworkID) {

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

			if(pst.executeUpdate() == 0){
				publishResult = PublishHomeworkResult.FAILED_DRAFT;
			}else{
				sql = "UPDATE `GroupHomework` SET groupHomeworkPublishDate = NOW() WHERE groupID = ? AND homeworkID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, homeworkID);
				
				if(pst.executeUpdate() == 0){
					publishResult = PublishHomeworkResult.FAILED_TO_UPDATE_GROUP;
				}else{
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
		
		Staff homeworkAuthor  = null;
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

				homeworkAuthor = new StaffService().getStaff(resultSet.getInt("teacherID"));
				homeworkTitle = resultSet.getString("homworkTitle");
				homeworkSubject	 = resultSet.getString("homeworkSubject");
				homeworkDescription	 = resultSet.getString("homeworkDescription");
				homeworkMinutesRequired = resultSet.getInt("homeworkMinutesRequired");
				homeworkDueDate = new Date(resultSet.getTimestamp("homeworkDueDate").getTime());
				homeworkDateCreated = new Date(resultSet.getTimestamp("homeworkDateCreated").getTime());
				homeworkIsOpen = resultSet.getInt("homeworkIsOpen") == 1 ? true : false;
				homeworkIsDraft = resultSet.getInt("homeworkIsDraft") == 1 ? true : false;
				homeworkIsTemplate = resultSet.getInt("homeworkIsTemplate") == 1 ? true : false;
				homeworkIsDeleted = resultSet.getInt("homeworkIsDeleted") == 1 ? true : false;
				homeworkDateDeleted = new Date(resultSet.getTimestamp("homeworkDateDeleted").getTime());

				homework = new Homework(homeworkID, homeworkAuthor, homeworkTitle, homeworkSubject,
						homeworkDescription,
						homeworkMinutesRequired, homeworkDueDate, homeworkIsOpen, homeworkDateCreated, homeworkIsDraft,
						homeworkIsTemplate, homeworkIsDeleted, homeworkDateDeleted);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homework;
	}

}

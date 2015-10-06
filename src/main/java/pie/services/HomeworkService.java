package pie.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import pie.Homework;
import pie.Staff;
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

				Staff homeworkAuthor = new StaffService().getStaff(resultSet.getInt("teacherID"));
				String homeworkTitle = resultSet.getString("homworkTitle");
				String homeworkSubject = resultSet.getString("homeworkSubject");
				String homeworkDescription = resultSet.getString("homeworkDescription");
				int homeworkMinutesRequired = resultSet.getInt("homeworkMinutesRequired");
				Date homeworkDueDate = new Date(resultSet.getTimestamp("homeworkDueDate").getTime());
				Date homeworkDateCreated = new Date(resultSet.getTimestamp("homeworkDateCreated").getTime());
				boolean homeworkIsOpen = resultSet.getInt("homeworkIsOpen")==1?true:false;
				boolean homeworkIsDraft = resultSet.getInt("homeworkIsDraft") == 1? true:false;
				boolean homeworkIsTemplate = resultSet.getInt("homeworkIsTemplate") == 1? true:false;
				boolean homeworkIsDeleted = resultSet.getInt("homeworkIsDeleted") == 1?true:false;
				Date homeworkDateDeleted = new Date(resultSet.getTimestamp("homeworkDateDeleted").getTime());
				
				homework = new Homework(homeworkID, homeworkAuthor, homeworkTitle, homeworkSubject, homeworkDescription, homeworkMinutesRequired, homeworkDueDate, homeworkIsOpen, 
						homeworkDateCreated, homeworkIsDraft, homeworkIsTemplate, homeworkIsDeleted, homeworkDateDeleted);
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

			String sql = "INSERT INTO  `Homework` (staffID ,homeworkTitle ,homeworkSubject ,homeworkDescription ,homeworkMinutesRequired "
					+ ",`homeworkDueDate`) VALUES (?,?,?,?,?,?)";
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
				homeworkID = resultSet.getInt(1);
				sql = "INSERT `GroupHomework` (staffID,groupID,homeworkID) VALUES(?,?,?)";
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

			String sql = "SELECT homeworkID FROM `Homework` WHERE staffID = ? AND homeworkIsDraft = ?";
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
		//determine if it is draft
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Homework` SET homeworkTitle = ?, homeworkSubject = ?, homeworkDescription = ?, int homeworkMinutesRequired, Date homeworkDueDate WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, homeworkTitle);
			pst.setString(2, homeworkSubject);
			pst.setString(3, homeworkDescription);
			pst.setInt(4, homeworkMinutesRequired);
			pst.setDate(5, homeworkDueDate);
			pst.setInt(6, homeworkID);

			if (pst.executeUpdate() != 0) {
				isUpdated = true;
			}
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	public boolean sendHomework(int groupID, int homeworkID) {

		boolean sendResult = false;

		GroupService groupService = new GroupService();
		Student[] groupStudents = groupService.getStudentMembers(groupID);

		for (Student student : groupStudents) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT `UserHomework` (homeworkID,userID) VALUES (?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, homeworkID);
				pst.setInt(2, student.getUserID());

				if (pst.executeUpdate() != 0) {
					sendResult = true;
				} 
				
				conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sendResult;
	}

	public boolean publishHomework(int groupID, int homeworkID) {

		boolean publishResult = false;
		
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Homework` SET homeworkIsDraft = ? , homeworkIsOpen = ? WHERE homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, 0);
			pst.setInt(3, homeworkID);
			
			pst.executeUpdate();

			if (pst.executeUpdate() != 0) {
				if (sendHomework(groupID, homeworkID)) {
					publishResult = true;
				}
			}
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publishResult;
	}

}

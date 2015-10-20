package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import pie.Homework;
import pie.User;
import pie.UserHomework;
import pie.utilities.DatabaseConnector;

public class UserHomeworkService {

	public UserHomework getUserHomework(int userHomeworkID){
		UserHomework userHomework = null;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `UserHomework` WHERE userHomework = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userHomeworkID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				UserService userService = new UserService();
				HomeworkService homeworkService = new HomeworkService();

				User user = userService.getUser(resultSet.getInt("userID"));
				Homework homework = homeworkService.getHomework(resultSet.getInt("homeworkID"));
				
				Date submissionDate = new Date(resultSet.getDate("submissionDate").getTime());
				Date dateArchived = new Date(resultSet.getDate("dateArchived").getTime());
				Date dateRead = new Date(resultSet.getDate("dateRead").getTime());
				boolean isSubmitted = resultSet.getInt("isSubmitted") == 1 ? true : false;
				boolean isArchived = resultSet.getInt("isArchived") == 1 ? true : false;
				boolean isDeleted = resultSet.getInt("isDeleted") == 1 ? true : false;
				boolean isRead = resultSet.getInt("isRead") == 1 ? true:false;
				boolean isMarked = resultSet.getInt("isMarked") == 1 ? true: false;
				String grade = resultSet.getString("grade");

				userHomework = new UserHomework(userHomeworkID, homework, user, isRead, isSubmitted, submissionDate, isArchived, dateArchived, dateRead, grade, isMarked, isDeleted);
			}

			conn.close();
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return userHomework;
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
	
	
	public boolean deleteHomework(int userHomeworkID){
		boolean deleteResult = false;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isDeleted = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userHomeworkID);
			
			pst.executeUpdate();
			
			deleteResult = true;
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return deleteResult;
	}
	
	public boolean markHomework(int userHomeworkID, int userID){
		boolean markedResult = false;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isMarked = ? WHERE userID = ? AND userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userID);
			pst.setInt(3, userHomeworkID);
			
			pst.executeUpdate();
			
			markedResult = true;
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return markedResult;
	}
	
	public boolean setHomeworkGrade(int userHomeworkID, int userID, String homeworkGrade){
		boolean gradeResult = false;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isMarked = ?, grade = ? WHERE userID = ? AND userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setString(2, homeworkGrade);
			pst.setInt(3, userID);
			pst.setInt(4, userHomeworkID);
			
			pst.executeUpdate();
			
			gradeResult = true;
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return gradeResult;
	}
	
	public UserHomework[] getUserHomeworkRecipients(int homeworkID, int groupHomeworkID, int publisherID){
		UserHomework[] userHomeworkRecipients = {};
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT userHomeworkID FROM `Homework`, `GroupHomework`, `UserHomework` WHERE `Homework`.homeworkID = `GroupHomework`.homeworkID AND `UserHomework`.homeworkID = `Homework`.homeworkID "
					+ "AND `GroupHomework`.groupHomeworkID = ? AND `GroupHomework`.publisherID = ? AND `UserHomework`.homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupHomeworkID);
			pst.setInt(2, publisherID);
			pst.setInt(3, homeworkID);
			
			resultSet = pst.executeQuery();
			
			ArrayList<UserHomework> tempUserHomeworkList = new ArrayList<UserHomework>();
			while(resultSet.next()){
				tempUserHomeworkList.add(getUserHomework(resultSet.getInt("userHomeworkID")));
			}
			
			userHomeworkRecipients = tempUserHomeworkList.toArray(userHomeworkRecipients);
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userHomeworkRecipients;
	}
	
	public boolean readHomework(int userHomeworkID){
		boolean readResult = false;

		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isRead = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userHomeworkID);
			
			pst.executeUpdate();
			
			readResult = true;
	public boolean archiveHomework(int userHomeworkID) {
		boolean archiveResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isArchived = ?, dateArchived = NOW() WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			archiveResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return archiveResult;
	}

	public boolean submitHomework(int userHomeworkID) {
		boolean submitResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isSubmitted = ?, submissionDate = NOW() WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			submitResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return submitResult;
	}
	
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return readResult;
	}
}

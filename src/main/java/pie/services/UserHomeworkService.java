package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import pie.GroupHomework;
import pie.Homework;
import pie.Parent;
import pie.Staff;
import pie.Student;
import pie.User;
import pie.UserHomework;
import pie.utilities.DatabaseConnector;

public class UserHomeworkService {

	public UserHomework getUserHomework(int userHomeworkID) {
		UserHomework userHomework = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `UserHomework` WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userHomeworkID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				UserService userService = new UserService();
				HomeworkService homeworkService = new HomeworkService();

				User user = userService.getUser(resultSet.getInt("userID"));
				Homework homework = homeworkService.getHomework(resultSet.getInt("homeworkID"));

				Date submissionDate = new Date(resultSet.getDate("submissionDate").getTime());
				boolean isSubmitted = resultSet.getInt("isSubmitted") == 1;
				boolean isArchived = resultSet.getInt("isArchived") == 1;
				boolean isDeleted = resultSet.getInt("isDeleted") == 1;
				boolean isRead = resultSet.getInt("isRead") == 1;
				boolean isMarked = resultSet.getInt("isMarked") == 1;
				String grade = resultSet.getString("grade");
				boolean isAcknowledged = resultSet.getInt("isAcknowledged") == 1;
				int groupHomeworkID = resultSet.getInt("groupHomeworkID");

				userHomework = new UserHomework(userHomeworkID, homework, user, isRead, isSubmitted, submissionDate,
						isArchived, grade, isMarked, isDeleted, isAcknowledged, groupHomeworkID);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userHomework;
	}

	public boolean createUserHomework(int homeworkID, int groupID) {

		boolean createUserHomeworkResult = false;
		
		Connection conn = DatabaseConnector.getConnection();
		Savepoint dbSavepoint = null;

		StaffGroupService staffGroupService = new StaffGroupService();
		StudentGroupService studentGroupService = new StudentGroupService();
		ParentStudentService parentStudentService = new ParentStudentService();
		
		Student[] studentMembers = studentGroupService.getStudentMembers(groupID);
		Staff[] staffMembers = staffGroupService.getStaffMembers(groupID);
		ArrayList<Integer> parentList = new ArrayList<Integer>();
		
		try {
			
			PreparedStatement pst = null;
			dbSavepoint = conn.setSavepoint("dbSavepoint");
			
			String sql = "INSERT INTO `UserHomework` (homeworkID, userID) VALUES (?,?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			for(Student student : studentMembers){
				
				int studentID = student.getUserID();
				
				pst.setInt(1, homeworkID);
				pst.setInt(2, studentID);
				pst.addBatch();
				
				Parent[] parents = parentStudentService.getParents(studentID);
				for(Parent currParent : parents){
					if(!parentList.contains(currParent.getUserID())){
						pst.setInt(1, homeworkID);
						pst.setInt(2, currParent.getUserID());
						pst.addBatch();
						parentList.add(currParent.getUserID());
					}
				}	
			}
			
			for(Staff staff : staffMembers){
				int staffID = staff.getUserID();
				
				pst.setInt(1, homeworkID);
				pst.setInt(2, staffID);
				pst.addBatch();
			}
			
			pst.executeBatch();
			
			conn.commit();
			createUserHomeworkResult = true;
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback(dbSavepoint);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return createUserHomeworkResult;
	}

	public boolean deleteHomework(int userHomeworkID) {
		boolean deleteResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isDeleted = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			deleteResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return deleteResult;
	}

	public boolean markHomework(int userHomeworkID, int userID, boolean isMarked) {
		boolean markedResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isMarked = ? WHERE userID = ? AND userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isMarked ? 1 : 0);
			pst.setInt(2, userID);
			pst.setInt(3, userHomeworkID);

			pst.executeUpdate();

			markedResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return markedResult;
	}

	public boolean setHomeworkGrade(int userHomeworkID, int userID, String homeworkGrade) {
		boolean gradeResult = false;

		try {

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gradeResult;
	}

	public UserHomework[] getUserHomeworkRecipients(int homeworkID, int groupHomeworkID, int publisherID) {
		UserHomework[] userHomeworkRecipients = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userHomeworkID FROM `Homework`, `GroupHomework`, `UserHomework` WHERE `Homework`.homeworkID = `GroupHomework`.homeworkID AND `UserHomework`.homeworkID = `Homework`.homeworkID "
					+ "AND `UserHomework`.groupHomeworkID = ? AND `GroupHomework`.publisherID = ? AND `UserHomework`.homeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupHomeworkID);
			pst.setInt(2, publisherID);
			pst.setInt(3, homeworkID);

			resultSet = pst.executeQuery();

			ArrayList<UserHomework> tempUserHomeworkList = new ArrayList<UserHomework>();
			while (resultSet.next()) {
				tempUserHomeworkList.add(getUserHomework(resultSet.getInt("userHomeworkID")));
			}

			userHomeworkRecipients = tempUserHomeworkList.toArray(userHomeworkRecipients);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userHomeworkRecipients;
	}

	public boolean readHomework(int userHomeworkID, boolean isRead) {
		boolean readResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isRead = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isRead ? 1 : 0);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			readResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return readResult;
	}

	public boolean archiveHomework(int userHomeworkID, boolean isArchived) {
		boolean archiveResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserHomework` SET isArchived = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isArchived ? 1 : 0);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			archiveResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return archiveResult;
	}

	public boolean submitHomework(int userHomeworkID, boolean isSubmitted) {
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

	public UserHomework[] getAllUserHomework(int userID) {
		UserHomework[] allUserHomework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userHomeworkID FROM `UserHomework` WHERE userID = ? AND isDeleted = ? ";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			pst.setInt(2, 0);

			resultSet = pst.executeQuery();

			ArrayList<UserHomework> tempUserHomeworkList = new ArrayList<UserHomework>();
			while (resultSet.next()) {
				tempUserHomeworkList.add(getUserHomework(resultSet.getInt("userHomeworkID")));
			}

			allUserHomework = tempUserHomeworkList.toArray(allUserHomework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allUserHomework;
	}
	
	public UserHomework[] getAllChildHomework(int userID) {
		UserHomework[] allUserHomework = {};

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userHomeworkID FROM `UserHomework` WHERE userID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);

			resultSet = pst.executeQuery();

			ArrayList<UserHomework> tempUserHomeworkList = new ArrayList<UserHomework>();
			while (resultSet.next()) {
				tempUserHomeworkList.add(getUserHomework(resultSet.getInt("userHomeworkID")));
			}

			allUserHomework = tempUserHomeworkList.toArray(allUserHomework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allUserHomework;
	}

	public UserHomework[] getAllMarkedUserHomework(int userID) {
		UserHomework[] allUserHomework = {};
		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userHomeworkID FROM `UserHomework` WHERE userID = ? and isDeleted = ? AND isMarked = ? AND grade IS NOT NULL";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			pst.setInt(2, 0);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			ArrayList<UserHomework> tempUserHomeworkList = new ArrayList<UserHomework>();
			while (resultSet.next()) {
				tempUserHomeworkList.add(getUserHomework(resultSet.getInt("userHomeworkID")));
			}

			allUserHomework = tempUserHomeworkList.toArray(allUserHomework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allUserHomework;
	}

	
	public UserHomework getChildHomework(int homeworkID, int userID){
		UserHomework userHomework = null;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT userHomeworkID FROM `UserHomework` WHERE homeworkID = ? AND userID = ?";
			
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);
			pst.setInt(2, userID);

			resultSet = pst.executeQuery();
			
			if(resultSet.next()){
				userHomework = getUserHomework(resultSet.getInt("userHomeworkID"));
			}
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userHomework;
	}
	
	public boolean setAcknowledged(int userHomeworkID, boolean isAcknowledged){
		boolean acknowledgeResult = false;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			String sql = "UPDATE `UserHomework` SET isAcknowledged = ? WHERE userHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isAcknowledged ? 1 : 0);
			pst.setInt(2, userHomeworkID);

			pst.executeUpdate();

			acknowledgeResult = true;

			conn.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return acknowledgeResult;

	}
	
	public String homeworkStatus(int userHomeworkID){
		
		String homeworkStatus = "N/A";
		
		GroupHomeworkService groupHomeworkService = new GroupHomeworkService();
		UserHomework userHomework = getUserHomework(userHomeworkID);
		
		if(!userHomework.isSubmitted()){
			try{

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;
				
				String sql = "SELECT DATEDIFF(NOW(), ?)";
				pst = conn.prepareStatement(sql);
				
				GroupHomework groupHomework = groupHomeworkService.getGroupHomework(userHomework.getGroupHomeworkID());
				
				pst.setDate(1, new java.sql.Date(groupHomework.getDueDate().getTime()));
				
				resultSet = pst.executeQuery();

				if(resultSet.next()){
					int difference = resultSet.getInt(1);
					if(difference == 0){
						homeworkStatus = "Processing";
					}else if(difference > 0){
						homeworkStatus = "Not Submitted";
					}
				}


				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			homeworkStatus = "Submitted";
		}
		
		

		
		
		
		return homeworkStatus;
	}
}

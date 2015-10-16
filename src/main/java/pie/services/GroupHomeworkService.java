package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import pie.Group;
import pie.GroupHomework;
import pie.Homework;
import pie.Staff;
import pie.utilities.DatabaseConnector;

public class GroupHomeworkService {

	public GroupHomework getGroupHomework(int groupHomeworkID) {
		
		GroupHomework groupHomework = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `GroupHomework` WHERE groupHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupHomeworkID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				StaffService staffService = new StaffService();
				GroupService groupService = new GroupService();
				HomeworkService homeworkService = new HomeworkService();

				Staff publisher = staffService.getStaff(resultSet.getInt("publisherID"));
				Group group = groupService.getGroup(resultSet.getInt("groupID"));
				Homework homework = homeworkService.getHomework(resultSet.getInt("homeworkID"));
				int markingEffort = resultSet.getInt("markingEffort");
				Date actualMarkingCompletionDate = new Date(resultSet.getDate("actualMarkingCompletionDate").getTime());
				Date targetMarkingCompletionDate = new Date(resultSet.getDate("targetMarkingCompletionDate").getTime());
				Date dueDate = new Date(resultSet.getDate("dueDate").getTime());
				Date publishDate = new Date(resultSet.getDate("publishDate").getTime());
				boolean isDraft = resultSet.getInt("isDraft") == 1 ? true : false;
				boolean isGraded = resultSet.getInt("isGraded") == 1 ? true : false;
				boolean isDeleted = resultSet.getInt("isDeleted") == 1 ? true : false;
				groupHomework = new GroupHomework(groupHomeworkID, group, homework, publisher, markingEffort,
						actualMarkingCompletionDate, targetMarkingCompletionDate, dueDate, publishDate, isDraft,
						isGraded, isDeleted);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return groupHomework;
	}

	public GroupHomework[] getAllSentHomework(int publisherID) {
		
		GroupHomework[] groupHomework = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupHomeworkID FROM `GroupHomework` WHERE publisherID = ? AND isDraft = ? AND isDeleted = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, publisherID);
			pst.setInt(2, 0);
			pst.setInt(3, 0);

			resultSet = pst.executeQuery();

			ArrayList<GroupHomework> tempGroupHomework = new ArrayList<GroupHomework>();
			while (resultSet.next()) {
				tempGroupHomework.add(getGroupHomework((resultSet.getInt("homeworkID"))));
			}

			groupHomework = tempGroupHomework.toArray(groupHomework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return groupHomework;
	}

	public GroupHomework[] getAllPublishedDraftHomework(int publisherID) {
		
		GroupHomework[] groupHomework = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupHomeworkID FROM `GroupHomework` WHERE publisherID = ? AND isDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, publisherID);
			pst.setInt(2, 1);

			resultSet = pst.executeQuery();

			ArrayList<GroupHomework> tempGroupHomework = new ArrayList<GroupHomework>();
			while (resultSet.next()) {
				tempGroupHomework.add(getGroupHomework((resultSet.getInt("homeworkID"))));
			}

			groupHomework = tempGroupHomework.toArray(groupHomework);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return groupHomework;
	}

	public boolean updatePublishDate(int homeworkID, int publisherID, int groupID) {
		
		boolean updateResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `GroupHomework` SET publishDate = NOW(), isDraft = ? WHERE homeworkID = ? AND publisherID = ? AND groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, homeworkID);
			pst.setInt(3, publisherID);
			pst.setInt(4, groupID);

			pst.executeUpdate();

			updateResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return updateResult;
	}
	
	public int sendPublishedHomework(GroupHomework groupHomework){
		int groupHomeworkID = -1;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `GroupHomework` (groupID, homeworkID, publisherID, markingEffort, actualMarkingCompeltionDate, targetMarkingDate, dueDate, publishDate, isDraft, isGraded)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, groupHomework.getGroup().getGroupID());
			pst.setInt(2, groupHomework.getHomework().getHomeworkID());
			pst.setInt(3, groupHomework.getPublisher().getUserID());
			pst.setInt(4, groupHomework.getMarkingEffort());
			pst.setDate(5, new java.sql.Date(groupHomework.getActualMarkingCompletionDate().getTime()));
			pst.setDate(6, new java.sql.Date(groupHomework.getTargetMarkingCompletionDate().getTime()));
			pst.setDate(7, new java.sql.Date(groupHomework.getDueDate().getTime()));
			pst.setInt(8, 0);
			pst.setInt(9, groupHomework.isGraded() == true ? 1:0);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				groupHomeworkID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return groupHomeworkID;
	}
	
	public int savePublishedHomeworkAsDraft(GroupHomework groupHomework){
		
		int groupHomeworkID = -1;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `GroupHomework` (groupID, homeworkID, publisherID, markingEffort, actualMarkingCompeltionDate, targetMarkingDate, dueDate, isGraded)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, groupHomework.getGroup().getGroupID());
			pst.setInt(2, groupHomework.getHomework().getHomeworkID());
			pst.setInt(3, groupHomework.getPublisher().getUserID());
			pst.setInt(4, groupHomework.getMarkingEffort());
			pst.setDate(5, new java.sql.Date(groupHomework.getActualMarkingCompletionDate().getTime()));
			pst.setDate(6, new java.sql.Date(groupHomework.getTargetMarkingCompletionDate().getTime()));
			pst.setDate(7, new java.sql.Date(groupHomework.getDueDate().getTime()));
			pst.setInt(8, groupHomework.isGraded() == true ? 1:0);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				groupHomeworkID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return groupHomeworkID;
	}
	
	public boolean deleteSentHomework(int groupHomeworkID){
		boolean deleteResult = false;
		
		GroupHomework groupHomework = getGroupHomework(groupHomeworkID);
		
		if(!groupHomework.isDraft()){
			try{
				
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				
				String sql = "UPDATE `GroupHomework` SET isDeleted = ? WHERE groupHomeworkID = ? AND isDraft = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 1);
				pst.setInt(2, groupHomeworkID);
				pst.setInt(3, 0);

				pst.executeUpdate();

				deleteResult = true;

				conn.close();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			try{
				
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				
				String sql = "DELETE FROM `GroupHomework` WHERE groupHomeworkID = ? AND isDraft = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupHomeworkID);
				pst.setInt(2, 1);
				
				pst.executeUpdate();
				
				deleteResult = true;
				
				conn.close();

			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return deleteResult;
	}
	
	
	public boolean updateDraftPublishedHomework(GroupHomework groupHomework){
		boolean updateResult = true;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			
			String sql = "UPDATE `GroupHomework` SET groupID = ?, markingEffort = ?, actualMarkingCompletionDate = ?, targetMarkingCompletionDate = ?, dueDate = ?, isGraded = ? WHERE groupHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupHomework.getGroup().getGroupID());
			pst.setInt(2, groupHomework.getMarkingEffort());
			pst.setDate(3, new java.sql.Date(groupHomework.getActualMarkingCompletionDate().getTime()));
			pst.setDate(4, new java.sql.Date(groupHomework.getTargetMarkingCompletionDate().getTime()));
			pst.setDate(5, new java.sql.Date(groupHomework.getDueDate().getTime()));
			pst.setInt(6, groupHomework.isGraded() == true ? 1:0);
			pst.setInt(7, groupHomework.getGroupHomeworkID());
			
			pst.executeUpdate();
			
			updateResult = true;
			
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return updateResult;
	}
	
	public boolean setDraftPublishedHomework(int groupHomeworkID){
		boolean setResult = false;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			
			String sql = "UPDATE `GroupHomework` SET isDraft = ? WHERE groupHomeworkID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, groupHomeworkID);
			
			
			pst.executeUpdate();
			
			setResult = true;
			
			conn.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return setResult;
	}
	
}

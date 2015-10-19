package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Group;
import pie.GroupNote;
import pie.Note;
import pie.Staff;
import pie.utilities.DatabaseConnector;


public class GroupNoteService {

	public GroupNote getGroupNote(int groupNoteID) {
		
		GroupNote groupNote = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `GroupNote` WHERE groupNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupNoteID);

			resultSet = pst.executeQuery();
			
			if(resultSet.next()) {
				
				Note note = new NoteService().getNote(resultSet.getInt("noteID"));
				Group group = new GroupService().getGroup(resultSet.getInt("groupID"));
				Staff staff = new StaffService().getStaff(resultSet.getInt("publisherID"));
				Date publishDate = new Date(resultSet.getDate("publishDate").getTime());
				
				groupNote = new GroupNote(groupNoteID, note, group, staff, publishDate);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return groupNote;
		
	}
	
	public int createGroupNote(int noteID, int groupID, int publisherID) {
		
		int groupNoteID = -1;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "INSERT INTO `GroupNote` (noteID, groupID, publisherID) VALUES (?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, noteID);
			pst.setInt(2, groupID);
			pst.setInt(3, publisherID);
			
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if(resultSet.next()) {
				groupNoteID = resultSet.getInt(1);
			}
			
			conn.close();
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		return groupNoteID;
		
	}
	
	public boolean publishGroupNote(int groupNoteID) {
		
		boolean isPublished = false;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			
			String sql = "UPDATE `GroupNote` SET publishDate = NOW() WHERE groupNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupNoteID);
			
			pst.executeUpdate();
			isPublished = true;
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isPublished;
		
	}
	
	
}
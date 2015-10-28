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
	
	public GroupNote getGroupNote(int userNoteID, int noteID) {
		
		GroupNote groupNote = null;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupNoteID FROM `Note`, `GroupNote`, `UserNote` WHERE `Note`.noteID = `GroupNote`.noteID AND `UserNote`.noteID = `Note`.noteID "
					+ "AND `UserNote`.userNoteID = ? AND `UserNote`.noteID = ? ";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userNoteID);
			pst.setInt(2, noteID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				GroupNoteService groupNoteService = new GroupNoteService();
				groupNote = groupNoteService.getGroupNote(resultSet.getInt("groupNoteID"));
			}	

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return groupNote;
	}
	
	public int getGroupNotes(int noteID) {
	
		int groupNoteID = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT groupNoteID FROM `GroupNote` WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				groupNoteID = resultSet.getInt("groupNoteID");
			}	

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return groupNoteID;
		
	}
	
	public int createGroupNote(int noteID, int groupID, int publisherID) {
		
		int groupNoteID = -1;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "INSERT INTO `GroupNote` (noteID, groupID, publisherID, publishDate) VALUES (?, ?, ?, NOW())";
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
	
}

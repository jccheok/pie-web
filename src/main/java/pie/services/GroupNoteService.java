package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
}

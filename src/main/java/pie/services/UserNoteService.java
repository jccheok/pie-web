package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Note;
import pie.ResponseOption;
import pie.User;
import pie.UserNote;
import pie.utilities.DatabaseConnector;

public class UserNoteService {

	public UserNote getUserNote(int userNoteID) {
		
		UserNote userNote = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `UserNote` WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userNoteID);

			resultSet = pst.executeQuery();
			
			if (resultSet.next()) {

				ResponseOption responseOption = new ResponseOptionService().getResponseOption(resultSet.getInt("responseOptionID"));
				Note note = new NoteService().getNote(resultSet.getInt("noteID"));
				User user = new UserService().getUser(resultSet.getInt("userID"));
				boolean isRead = resultSet.getInt("isRead") == 1;
				boolean isArchive = resultSet.getInt("isArchive") == 1;
				Date dateRead = new Date(resultSet.getTimestamp("dateRead").getTime());
				Date dateArchive = new Date(resultSet.getTimestamp("dateArchive").getTime());
				String responseText = resultSet.getString("responseText");
				
				userNote = new UserNote(userNoteID, responseOption, note, user, isRead, isArchive, dateRead, dateArchive, responseText);
				
			}

			conn.close();
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		return userNote;
		
	}
	
	public int createUserNote(int responseOptionID, int noteID, int userID) {
		
		int userNoteID = -1;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "INSERT INTO `UserNote` (responseOptionID, noteID, userID) VALUES (?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, responseOptionID);
			pst.setInt(2, noteID);
			pst.setInt(3, userID);
			
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if(resultSet.next()) {
				userNoteID = resultSet.getInt(1);
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userNoteID;
		
	}
	
	public boolean isRead(int userNoteID) {
		
		boolean isRead = false;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			
			String sql = "UPDATE `UserNote` SET isRead = 1, SET dateRead = NOW() WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userNoteID);
			
			pst.executeUpdate();
			isRead = true;
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isRead;
		
	}
	
	public boolean isArchive(int userNoteID) {
		
		boolean isArchive = false;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			
			String sql = "UPDATE `UserNote` SET isArchive = 1, SET dateArchive = NOW() WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userNoteID);
			
			pst.executeUpdate();
			isArchive = true;
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isArchive;
		
	}
	
}

package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

import pie.Note;
import pie.ResponseOption;
import pie.Staff;
import pie.Student;
import pie.User;
import pie.UserNote;
import pie.utilities.DatabaseConnector;

public class UserNoteService {

	public UserNote getUserNote(int userNoteID) {

		UserNote userNote = null;

		ResponseService responseService =  new ResponseService();
		NoteService noteService = new NoteService();
		UserService userService = new UserService();
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `UserNote` WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userNoteID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				ResponseOption noteResponseOption = responseService.getResponseOption(resultSet.getInt("responseOptionID"));
				Note note = noteService.getNote(resultSet.getInt("noteID"));
				User noteRecipient = userService.getUser(resultSet.getInt("userID"));
				boolean noteIsRead = resultSet.getInt("isRead") == 1;
				boolean noteIsArchived = resultSet.getInt("isArchive") == 1;
				String noteResponseText = resultSet.getString("responseText");

				userNote = new UserNote(userNoteID, noteResponseOption, note, noteRecipient, noteIsRead, noteIsArchived, noteResponseText);

			}

			conn.close();

		} catch (Exception e ) {
			e.printStackTrace();
		}

		return userNote;

	}

	public boolean setUserNoteRead(int userNoteID, boolean isRead) {

		boolean setResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserNote` SET isRead = ? WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isRead ? 1 : 0);
			pst.setInt(2, userNoteID);

			pst.executeUpdate();

			setResult = true;

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return setResult;
	}

	public boolean setUserNoteArchived(int userNoteID, boolean isArchived) {

		boolean setResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserNote` SET isArchive = ? WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isArchived ? 1 : 0);
			pst.setInt(2, userNoteID);

			pst.executeUpdate();

			setResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return setResult;

	}

	public boolean setUserNoteResponse(int userNoteID, int responseOptionID, String responseText) {

		boolean setResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `UserNote` SET responseOptionID = ?, responseText = ? WHERE userNoteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, responseOptionID);
			pst.setString(2, responseText);
			pst.setInt(3, userNoteID);

			pst.executeUpdate();
			setResult = true;

			conn.close();

		} catch (Exception e ) {
			e.printStackTrace();
		}

		return setResult;
	}

	public int createUserNote(int noteID, int userID) {

		int userNoteID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `UserNote` (noteID, userID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, noteID);
			pst.setInt(2, userID);
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

	public UserNote[] getAllUserNote(int userID) {

		UserNote[] allUserNote = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT userNoteID FROM `UserNote` WHERE userID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);

			resultSet = pst.executeQuery();

			ArrayList<UserNote> tempUserNoteList = new ArrayList<UserNote>();
			while(resultSet.next()) {
				tempUserNoteList.add(getUserNote(resultSet.getInt("userNoteID")));
			}

			allUserNote = tempUserNoteList.toArray(allUserNote);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allUserNote;

	}

	public boolean createUserNoteStudent(int noteID, Student[] user) {

		boolean isSuccess = false;
		Connection conn = DatabaseConnector.getConnection();
		Savepoint dbSavepoint = null;

		try {
			
			PreparedStatement pst = null;
			conn.setAutoCommit(false);
			dbSavepoint = conn.setSavepoint("dbSavepoint");

			String sql = "INSERT INTO `UserNote` (noteID, userID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql);

			for(Student student : user) {

				pst.setInt(1, noteID);
				pst.setInt(2, student.getUserID());
				pst.addBatch();

			}

			pst.executeBatch();
			conn.commit();
			
			isSuccess = true;
			conn.close();

		} catch (SQLException e) {
			
			try {
				conn.rollback(dbSavepoint);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}

		return isSuccess;

	}
	
	public boolean createUserNoteStaff(int noteID, Staff[] user) {

		boolean isSuccess = false;
		Connection conn = DatabaseConnector.getConnection();
		Savepoint dbSavepoint = null;

		try {

			PreparedStatement pst = null;
			conn.setAutoCommit(false);
			dbSavepoint = conn.setSavepoint("dbSavepoint");

			String sql = "INSERT INTO `UserNote` (noteID, userID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql);

			for(Staff staff : user) {

				pst.setInt(1, noteID);
				pst.setInt(2, staff.getUserID());
				pst.executeUpdate();

			}

			conn.commit();
			isSuccess = true;
			conn.close();

		} catch (SQLException e) {
			
			try {
				conn.rollback(dbSavepoint);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}

		return isSuccess;

	}
	

}

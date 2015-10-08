 package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import pie.Note;
import pie.Staff;
import pie.Student;
import pie.constants.PublishNoteResult;
import pie.utilities.DatabaseConnector;

public class NoteService {

	public Note getNote(int noteID) {

		Note note = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Note` WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				Staff noteAuthor = new StaffService().getStaff(resultSet.getInt("teacherID"));
				String noteTitle = resultSet.getString("noteTitle");
				String noteDescription = resultSet.getString("noteDescription");
				boolean noteIsTemplate = resultSet.getInt("noteIsTemplate") == 1;
				boolean noteIsDraft = resultSet.getInt("noteIsDraft") == 1;
				boolean noteIsDeleted = resultSet.getInt("noteIsDeleted") == 1;
				Date noteDateCreated = new Date(resultSet.getTimestamp("noteDateCreated").getTime());
				Date noteDateDeleted = new Date(resultSet.getTimestamp("noteDateDeleted").getTime());

				note = new Note(noteID, noteAuthor, noteTitle, noteDescription, noteIsTemplate, noteIsDraft,
						noteIsDeleted, noteDateCreated, noteDateDeleted);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return note;
	}

	public int createNote(int staffID, int responseQuestionID, String noteTitle, String noteDescription, int groupID) {

		int noteID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Note` (staffID, responseQuestionID, noteTitle, noteDescription) VALUES (?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, staffID);
			pst.setInt(2, responseQuestionID);
			pst.setString(3, noteTitle);
			pst.setString(4, noteDescription);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if(resultSet.next()) {
				noteID = resultSet.getInt(1);

				sql = "INSERT INTO `GroupNote` (staffID, noteID, groupID) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, staffID);
				pst.setInt(2, noteID);
				pst.setInt(3, groupID);
				pst.executeUpdate();
			}

			conn.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return noteID;
	}

	public boolean hasReceived(int noteID, int userID) {

		boolean hasReceived = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `UserNote` WHERE userID = ? and noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			pst.setInt(2, noteID);

			resultSet = pst.executeQuery();

			if(resultSet.next()){
				hasReceived = true;
			}

			conn.close();

		} catch (Exception e) {

			System.out.println(e);
		}

		return hasReceived;
	}

	public boolean sendNote(int noteID, int studentID) {

		boolean sendResult = false;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "INSERT `UserNote` (noteID, userID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);
			pst.setInt(2, studentID);

			pst.executeUpdate();
			
			sendResult = true;
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sendResult;
	}

	public PublishNoteResult publishNote(int noteID, int groupID) {

		PublishNoteResult publishResult = PublishNoteResult.SUCCESS;
		GroupService groupService = new GroupService();

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Note` SET noteIsDraft = ? WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, noteID);
			
			pst.executeUpdate();
			if(pst.executeUpdate() == 0){
				publishResult = PublishNoteResult.FAILED_DRAFT;
			}else{
				sql = "UPDATE `GroupNote` SET groupNotePublishDate = NOW() WHERE groupID = ? AND noteID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, noteID);
				
				if(pst.executeUpdate() == 0){
					publishResult = PublishNoteResult.FAILED_TO_UPDATE_GROUP;
				}else{
					Student[] groupStudents = groupService.getStudentMembers(groupID);
					
					for (Student student : groupStudents) {
						if (!sendNote(noteID, student.getUserID())) {
							publishResult = PublishNoteResult.FAILED_TO_SEND_TO_MEMBERS;
						}
					}
				}
			}

			conn.close();

		} catch (Exception e) {

			System.out.println(e);
		}

		return publishResult;
	}
	
	public boolean deleteNote(int noteID) {

		boolean isDeleted = false;
		
		try {

			PreparedStatement pst = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "UPDATE `Note` SET noteIsDeleted = ? WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, noteID);
			
			pst.executeUpdate();
			isDeleted = true;

			conn.close();

		} catch(Exception e) {

			System.out.println(e);
		}
		
		return isDeleted;
	}
	
	public Note[] getNoteDrafts(int staffID) {

		Note[] noteDrafts = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT noteID FROM `Note` WHERE staffID = ? AND noteIsDraft = 1 AND noteIsDeleted = 0";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			resultSet = pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();
			
			while(resultSet.next()){

				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			conn.close();

			noteDrafts = tempNotes.toArray(noteDrafts);
		}

		catch(Exception e){
			System.out.println(e);
		}
	
		return noteDrafts;
	}
	
	public Note[] getSentNotes(int staffID) {

		Note[] sentNotes = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT noteID FROM `Note` WHERE staffID = ? AND noteIsDraft = 0 AND noteIsDeleted = 0";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, staffID);
			resultSet = pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();
			
			while(resultSet.next()){

				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			conn.close();

			sentNotes = tempNotes.toArray(sentNotes);
		}

		catch(Exception e){
			System.out.println(e);
		}
	
		return sentNotes;
	}
	
	public Note[] getNotes(int userID) {
		
		Note[] notes = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT `Note`.noteID FROM `UserNote`,`Note` WHERE userID=? AND `Note`.noteID = `UserNote`.noteID AND noteIsDeleted = 0";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			resultSet=pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();

			while(resultSet.next()) {
				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			notes = tempNotes.toArray(notes);


			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return notes;
	}
	

}
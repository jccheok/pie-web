package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Note;
import pie.ResponseQuestion;
import pie.Staff;
import pie.Student;
import pie.constants.DeleteNoteResult;
import pie.constants.PublishNoteResult;
import pie.utilities.DatabaseConnector;

public class NoteService {

	public Note getNote(int noteID) {
		
		Note note = null;
		
		StaffService staffService = new StaffService();
		ResponseService responseService = new ResponseService();

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Note` WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				Staff noteAuthor = staffService.getStaff(resultSet.getInt("authorID"));
				String noteTitle = resultSet.getString("title");
				String noteDescription = resultSet.getString("description");
				boolean noteIsDraft = resultSet.getInt("isDraft") == 1;
				boolean noteIsDeleted = resultSet.getInt("isDeleted") == 1;
				Date noteDateCreated = new Date(resultSet.getTimestamp("dateCreated").getTime());
				Date noteDateDeleted = new Date(resultSet.getTimestamp("dateDeleted").getTime());
				ResponseQuestion noteResponseQuestion = responseService.getResponseQuestion(resultSet.getInt("responseQuestionID"));

				note = new Note(noteID, noteAuthor, noteTitle, noteDescription, noteIsDraft, noteIsDeleted, noteDateCreated, noteDateDeleted, noteResponseQuestion);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return note;
	}

	public int createNote(int authorID, int responseQuestionID, String title, String description) {

		int noteID = -1;
		
		if(description == null || description.length() == 0){
			description = "No Description";
		}

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Note` (authorID, responseQuestionID, title, description) VALUES (?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, authorID);
			pst.setInt(2, responseQuestionID);
			pst.setString(3, title);
			pst.setString(4, description);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if (resultSet.next()) {
				noteID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return noteID;
	}

	public PublishNoteResult publishNote(int noteID, int groupID, int publisherID) {

		PublishNoteResult publishResult = PublishNoteResult.SUCCESS;
		
		StaffGroupService staffGroupService = new StaffGroupService();
		StudentGroupService studentGroupService = new StudentGroupService();
		GroupNoteService groupNoteService = new GroupNoteService();
		UserNoteService userNoteService = new UserNoteService();

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Note` SET isDraft = ? WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, noteID);

			pst.executeUpdate();
			
			int groupNoteID = groupNoteService.createGroupNote(noteID, groupID, publisherID);

			if (groupNoteID == -1) {

				publishResult = PublishNoteResult.FAILED_TO_UPDATE_GROUP;

			} else {

				Student[] groupStudents = studentGroupService.getStudentMembers(groupID);

				for (Student student : groupStudents) {
					if (userNoteService.createUserNote(noteID, student.getUserID()) == -1) {
						publishResult = PublishNoteResult.FAILED_TO_SEND_TO_MEMBERS;
					}
				}

				Staff[] groupStaffs = staffGroupService.getStaffMembers(groupID);

				for (Staff staff : groupStaffs) {
					if (userNoteService.createUserNote(noteID, staff.getUserID()) == -1) {
						publishResult = PublishNoteResult.FAILED_TO_SEND_TO_MEMBERS;
					}
				}
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publishResult;
	}

	public DeleteNoteResult deleteNote(int noteID) {

		DeleteNoteResult deleteNoteResult = DeleteNoteResult.SUCCESS;
		
		try {

			Note note = getNote(noteID);
			if(note == null) {	
				deleteNoteResult = DeleteNoteResult.NOTE_DOES_NOT_EXIST;
			} else if(note.isDraft()) {

				if(!deleteDraftNote(noteID)) {
					deleteNoteResult = DeleteNoteResult.FAILED_REMOVE_NOTE;
				}

			} else {

				if(!deletePublishedNote(noteID)) {
					deleteNoteResult = DeleteNoteResult.FAILED_TO_SET_TO_DELETE;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		return deleteNoteResult;
	}

	public Note[] getNoteDrafts(int authorID) {

		Note[] noteDrafts = {};
		
		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT noteID FROM `Note` WHERE authorID = ? AND isDraft = ? AND isDeleted = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, authorID);
			pst.setInt(2, 1);
			pst.setInt(3, 0);
			resultSet = pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();

			while (resultSet.next()) {

				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			conn.close();

			noteDrafts = tempNotes.toArray(noteDrafts);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return noteDrafts;
	}

	public Note[] getSentNotes(int authorID) {

		Note[] sentNotes = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT noteID FROM `Note` WHERE authorID = ? AND isDraft = 0 AND isDeleted = 0";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, authorID);
			resultSet = pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();

			while (resultSet.next()) {

				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			conn.close();

			sentNotes = tempNotes.toArray(sentNotes);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return sentNotes;
	}

	public Note[] getNotes(int userID, int startNote, int endNote) {

		Note[] notes = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT `Note`.noteID FROM `UserNote`,`Note` WHERE userID = ? AND `Note`.noteID = `UserNote`.noteID AND isDeleted = 0 LIMIT ?, ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			pst.setInt(2, startNote);
			pst.setInt(3, endNote);
			resultSet = pst.executeQuery();

			ArrayList<Note> tempNotes = new ArrayList<Note>();

			while (resultSet.next()) {
				tempNotes.add(getNote(resultSet.getInt(1)));
			}

			notes = tempNotes.toArray(notes);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return notes;
	}

	public boolean updateNote(String newTitle, String newNoteDescription, int newResponseQuestionID, int noteID) {

		boolean isUpdated = false;

		try {

			PreparedStatement pst = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "UPDATE `Note` SET title = ?, description = ?, responseQuestionID = ? WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, newTitle);
			pst.setString(2, newNoteDescription);
			pst.setInt(3, newResponseQuestionID);
			pst.setInt(4, noteID);

			pst.executeUpdate();
			isUpdated = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	public boolean deletePublishedNote(int noteID){
		boolean deleteNoteResult = false;

		try {

			PreparedStatement pst = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "UPDATE `Note` SET isDeleted = ?, dateDeleted = NOW() WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, noteID);

			pst.executeUpdate();
			deleteNoteResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return deleteNoteResult;
	}

	public boolean deleteDraftNote(int noteID){
		boolean deleteNoteResult = false;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "DELETE FROM `Note` WHERE noteID = ? AND isDraft = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);
			pst.setInt(2, 1);

			pst.executeUpdate();

			deleteNoteResult = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return deleteNoteResult;
	}
	
	public PublishNoteResult publishNote(int noteID, String groupIDList, int publisherID) {

		PublishNoteResult publishResult = PublishNoteResult.SUCCESS;
		
		StaffGroupService staffGroupService = new StaffGroupService();
		StudentGroupService studentGroupService = new StudentGroupService();
		GroupNoteService groupNoteService = new GroupNoteService();
		UserNoteService userNoteService = new UserNoteService();

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `Note` SET isDraft = ? WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 0);
			pst.setInt(2, noteID);

			pst.executeUpdate();
			
			boolean groupNoteSuccess = groupNoteService.createGroupNote(noteID, groupIDList, publisherID);

			if (groupNoteSuccess == false) {

				publishResult = PublishNoteResult.FAILED_TO_UPDATE_GROUP;

			} else {
				
				JSONObject requestObject = new JSONObject(groupIDList);
				JSONArray groupList = requestObject.getJSONArray("groupIDArray");

				for (int index = 0; index < groupList.length(); index++) {
					JSONObject group = groupList.getJSONObject(index);
					int groupID = group.getInt("groupID");
					
					Student[] groupStudents = studentGroupService.getStudentMembers(groupID);

					if (userNoteService.createUserNoteStudent(noteID, groupStudents) == false) {
						publishResult = PublishNoteResult.FAILED_TO_SEND_TO_MEMBERS;
					}
					
					Staff[] groupStaffs = staffGroupService.getStaffMembers(groupID);
					
					if (userNoteService.createUserNoteStaff(noteID, groupStaffs) == false) {
						publishResult = PublishNoteResult.FAILED_TO_SEND_TO_MEMBERS;
					}
						
				}
				
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publishResult;
	}

}

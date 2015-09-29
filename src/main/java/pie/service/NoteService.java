package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Group;
import pie.Note;
import pie.Staff;
import pie.User;
import pie.util.DatabaseConnector;

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
				note = new Note();
				note.setNoteID(noteID);
				note.setNoteAuthor(new StaffService().getStaff(resultSet
						.getInt("staffID")));
				note.setNoteTitle(resultSet.getString("noteTitle"));
				note.setNoteDescription(resultSet.getString("noteDescription"));
				note.setNoteIsTemplate(resultSet.getInt("noteIsTemplate") == 1);
				note.setNoteIsDraft(resultSet.getInt("noteIsDraft") == 1);
				note.setNoteIsDeleted(resultSet.getInt("noteIsDeleted") == 1);
				note.setNoteDateCreated(new Date(resultSet.getTimestamp(
						"noteDateCreated").getTime()));
				note.setNoteDateDeleted(new Date(resultSet.getTimestamp(
						"noteDateDeleted").getTime()));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return note;
	}

	public Group[] getGroupRecipient(int noteID) {
		Group[] groupRecipient = {};

		// Write codes to retrieve Group Recipients of Note

		return groupRecipient;
	}

	public User[] getUserRecipient(int noteID) {
		User[] userRecipient = {};

		// Write codes to retrieve User Recipients of Note

		return userRecipient;
	}

	public int createNote(Staff staff, String noteTitle,
			String noteDescription) {
		int noteID = -1;

		// Write codes to create Note

		return noteID;
	}

	public int createNoteAsDraft(Staff staff, String noteTitle,
			String noteDescription) {
		int noteID = -1;
		int noteIsDraft = 1;

		// Write codes to create Note as Draft

		return noteID;
	}

	public int createNoteAsTemplate(Staff staff, String noteTitle,
			String noteDescription) {
		int noteID = -1;
		int noteIsTemplate = 1;

		// Write codes to create Note as Template;

		return noteID;
	}

	public boolean deleteNote(int noteID) {
		boolean deleteResult = false;
		int noteIsDeleted = 1;

		// Write codes to delete Note

		return deleteResult;
	}

	public boolean sendDraftNote(int noteID) {
		boolean sendResult = false;
		int noteIsDraft = 0;

		// Write codes to send Note and set NoteAsDraft to false

		return sendResult;
	}

	public Note[] getNotesIsDraft(Staff staff) {
		Note[] notes = {};

		// Write codes to retrieve Notes that are drafted by Staff

		return notes;
	}

	public Note[] getNotesIsTemplate(Staff staff) {
		Note[] notes = {};

		// Write codes to retrieve Notes that are saved as Template by Staff

		return notes;
	}
	
}
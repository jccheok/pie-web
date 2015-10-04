package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import pie.Note;
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
				
				note = new Note();
				note.setNoteID(noteID);
				note.setNoteAuthor(new StaffService().getStaff(resultSet.getInt("teacherID")));
				note.setNoteTitle(resultSet.getString("noteTitle"));
				note.setNoteDescription(resultSet.getString("noteDescription"));
				note.setNoteIsTemplate(resultSet.getInt("noteIsTemplate") == 1);
				note.setNoteIsDraft(resultSet.getInt("noteIsDraft") == 1);
				note.setNoteIsDeleted(resultSet.getInt("noteIsDeleted") == 1);
				note.setNoteDateCreated(new Date(resultSet.getTimestamp("noteDateCreated").getTime()));
				note.setNoteDateDeleted(new Date(resultSet.getTimestamp("noteDateDeleted").getTime()));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return note;
	}
}
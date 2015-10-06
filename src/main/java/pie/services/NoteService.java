package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import pie.Group;
import pie.GroupType;
import pie.Note;
import pie.School;
import pie.Staff;
import pie.Student;
import pie.User;
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

	public Note createNote(int staffID, int responseQuestionID, String noteTitle, String noteDescription, int groupID) {

		Note newNote = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO 'Note' (staffID, responseQuestionID, noteTitle, noteDescription) VALUES (?, ?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, staffID);
			pst.setInt(2, responseQuestionID);
			pst.setString(3, noteTitle);
			pst.setString(4, noteDescription);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();

			if(resultSet.next()) {
				int noteID = resultSet.getInt(1);
				newNote = getNote(noteID);

				sql = "INSERT INTO 'GroupNote' (staffID, noteID, groupID) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, staffID);
				pst.setInt(2, newNote.getNoteID());
				pst.setInt(3, groupID);
				pst.executeUpdate();

				resultSet = pst.getGeneratedKeys();

			}

			conn.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return newNote;
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

			hasReceived = resultSet.next();

			conn.close();

		} catch (Exception e) {

			System.out.println(e);
		}

		return hasReceived;
	}

	public boolean sendNote(int groupID, int noteID) {

		boolean sendResult = false;
		GroupService groupService = new GroupService();
		Student[] groupStudents = groupService.getStudentMembers(groupID);

		for(Student student: groupStudents) {
			try {
				
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT 'UserNote' (noteID, userID) VALUES (?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, noteID);
				pst.setInt(2, student.getUserID());

				if(pst.executeUpdate() != 0) {
					sendResult = true;
				} 

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sendResult;
	}

	public boolean publishNote(int noteID, int groupID) {

		boolean publishResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `GroupNote` SET groupNotePublishDate = NOW() WHERE noteID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);

			if(pst.executeUpdate() != 0) {
				if(sendNote(noteID, groupID)) {
					publishResult = true;
				}
			}

			conn.close();

		} catch (Exception e) {

			System.out.println(e);
		}

		return publishResult;

	}

}
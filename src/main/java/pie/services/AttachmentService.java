package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Part;

import pie.HomeworkAttachment;
import pie.NoteAttachment;
import pie.utilities.DatabaseConnector;

public class AttachmentService {

	public int createNoteAttachment(String attachmentURL) {

		int noteAttachmentID = -1;
		int tempNoteID = 1;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `NoteAttachment` (attachmentURL, noteID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, attachmentURL);
			pst.setInt(2, tempNoteID);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();
			if (resultSet.next()) {
				noteAttachmentID = resultSet.getInt(1);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return noteAttachmentID;
	}
	
	public int createHomeworkAttachment(String attachmentURL) {

		int homeworkAttachmentID = -1;
		int tempHomeworkID = 1;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `HomeworkAttachment` (attachmentURL, homeworkID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, attachmentURL);
			pst.setInt(2, tempHomeworkID);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();
			if (resultSet.next()) {
				homeworkAttachmentID = resultSet.getInt(1);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkAttachmentID;
	}
	
	public NoteAttachment getNoteAttachment(int noteAttachmentID) {
		
		NoteAttachment noteAttachment = null;
		String attachmentURL = null;
		int noteID = 0;	
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `NoteAttachment` WHERE noteAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteAttachmentID);
			resultSet = pst.executeQuery();
			
			if(resultSet.next()) {
				attachmentURL = resultSet.getString("attachmentURL");
				noteID = resultSet.getInt("noteID");
				
				noteAttachment = new NoteAttachment(noteAttachmentID, attachmentURL, noteID);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return noteAttachment;	
	}
	
	public HomeworkAttachment getHomeworkAttachment(int homeworkAttachmentID) {
		
		HomeworkAttachment homeworkAttachment = null;
		String attachmentURL = null;
		int homeworkID = 0;	
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `HomeworkAttachment` WHERE homeworkAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkAttachmentID);
			resultSet = pst.executeQuery();
			
			if(resultSet.next()) {
				attachmentURL = resultSet.getString("attachmentURL");
				homeworkID = resultSet.getInt("homeworkID");
				
				homeworkAttachment = new NoteAttachment(homeworkAttachmentID, attachmentURL, homeworkID);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return homeworkAttachment;	
	}

	public String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return null;
    }

}

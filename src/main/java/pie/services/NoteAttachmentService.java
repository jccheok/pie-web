package pie.services;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Part;

import pie.Note;
import pie.NoteAttachment;
import pie.utilities.DatabaseConnector;

public class NoteAttachmentService {

	public int createNoteAttachment(String noteAttachmentURL, int noteID) {

		int noteAttachmentID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `NoteAttachment` (attachmentURL, noteID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, noteAttachmentURL);
			pst.setInt(2, noteID);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();
			if (resultSet.next()) {
				noteAttachmentID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return noteAttachmentID;
	}

	public NoteAttachment getNoteAttachment(int noteAttachmentID) {

		NoteAttachment noteAttachment = null;
		String attachmentURL = null;
		Note note = null;	

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
				note = new NoteService().getNote(resultSet.getInt("noteID"));

				noteAttachment = new NoteAttachment(noteAttachmentID, attachmentURL, note);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return noteAttachment;	
	}

	public boolean updateNoteAttachmentID(int noteAttachmentID, int noteID) {

		boolean isUpdated = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `NoteAttachment` SET noteID = ? WHERE noteAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, noteID);
			pst.setInt(2, noteAttachmentID);

			pst.executeUpdate();
			isUpdated = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}
	
	public String updateNoteAttachmentName(int noteAttachmentID, String noteAttachmentURL) {
		
		String newNoteAttachmentURL = "" + noteAttachmentID + "-" + noteAttachmentURL;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `NoteAttachment` SET attachmentURL = ? WHERE noteAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, newNoteAttachmentURL);
			pst.setInt(2, noteAttachmentID);

			pst.executeUpdate();

			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newNoteAttachmentURL;
	}
	
	public String getNoteFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
			}
		}
		return null;
	}
	
	public boolean deleteNoteAttachment(String noteAttachmentURL) {
		
		boolean isDeleted = false;
		
		File noteAttachmentDIR = new File(getNoteAttachmentDIR(noteAttachmentURL));
		if(noteAttachmentDIR.exists()) {
			
			try {
				
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "DELETE FROM `NoteAttachment` WHERE attachmentURL = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, noteAttachmentURL);
				
				pst.executeUpdate();
				
				noteAttachmentDIR.delete();
				isDeleted = true;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isDeleted;
	}
	
	public boolean checkIfNoteFolderExist() {
		
		boolean isExist = false;
		
		File noteAttachmentDIR = new File(getNoteDIR());
		if(noteAttachmentDIR.exists()) {
			noteAttachmentDIR.mkdir();
			
			isExist = true;
		}
		
		return isExist;
	}
	
	public String getNoteDIR() {

		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
		String uploadedDir = uploadPath + File.separator + "uploadedNoteDIR";

		return uploadedDir;	
	}

	public String getNoteAttachmentDIR(String noteAttachmentURL) {
		
		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
		String uploadedDir = uploadPath + File.separator + "uploadedNoteDIR" + File.separator + noteAttachmentURL;
		
		return uploadedDir;
	}

}

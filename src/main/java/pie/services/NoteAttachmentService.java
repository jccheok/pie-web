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

	public int createNoteAttachment(String noteAttachmentURL) {

		int noteAttachmentID = -1;
		int tempNoteID = 1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `NoteAttachment` (attachmentURL, noteID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, noteAttachmentURL);
			pst.setInt(2, tempNoteID);
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

	public boolean UpdateNoteAttachmentID(int noteAttachmentID, int noteID) {

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
		NoteAttachmentService noteAttachmentService = new NoteAttachmentService();
		
		File noteAttachmentDIR = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
		if(!noteAttachmentDIR.exists()) {
			noteAttachmentDIR.delete();
			
			isDeleted = true;
		}

		return isDeleted;
	}
	
	public boolean checkIfNoteFolderExist() {
		
		boolean isExist = false;
		NoteAttachmentService noteAttachmentService = new NoteAttachmentService();
		
		File noteAttachmentDIR = new File(noteAttachmentService.getNoteDIR());
		if(!noteAttachmentDIR.exists()) {
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

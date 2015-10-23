package pie.services;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Part;

import pie.Homework;
import pie.HomeworkAttachment;
import pie.utilities.DatabaseConnector;

public class HomeworkAttachmentService {

	public int createHomeworkAttachment(String homeworkAttachmentURL, int homeworkID) {

		int homeworkAttachmentID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `HomeworkAttachment` (attachmentURL, homeworkID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, homeworkAttachmentURL);
			pst.setInt(2, homeworkID);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();
			if (resultSet.next()) {
				homeworkAttachmentID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkAttachmentID;
	}

	public HomeworkAttachment getHomeworkAttachment(int homeworkAttachmentID) {

		HomeworkAttachment homeworkAttachment = null;
		String attachmentURL = null;
		Homework homework = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `HomeworkAttachment` WHERE homeworkAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkAttachmentID);
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				attachmentURL = resultSet.getString("attachmentURL");
				homework = new HomeworkService().getHomework(resultSet.getInt("homeworkID"));

				homeworkAttachment = new HomeworkAttachment(homeworkAttachmentID, attachmentURL, homework);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return homeworkAttachment;
	}

	public boolean updateHomeworkAttachmentID(int homeworkAttachmentID, int homeworkID) {

		boolean isUpdated = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `HomeworkAttachment` SET homeworkID = ? WHERE homeworkAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, homeworkID);
			pst.setInt(2, homeworkAttachmentID);

			pst.executeUpdate();
			isUpdated = true;

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	public String updateHomeworkAttachmentName(int homeworkAttachmentID, String homeworkAttachmentURL) {

		String newHomeworkAttachmentURL = homeworkAttachmentID + "-" + homeworkAttachmentURL;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "UPDATE `HomeworkAttachment` SET attachmentURL = ? WHERE homeworkAttachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, newHomeworkAttachmentURL);
			pst.setInt(2, homeworkAttachmentID);

			pst.executeUpdate();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return newHomeworkAttachmentURL;
	}

	public String getHomeworkFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return null;
	}

	public boolean deleteHomeworkAttachment(String homeworkAttachmentURL) {

		boolean isDeleted = false;

		File homeworkAttachmentDIR = new File(getHomeworkAttachmentDIR(homeworkAttachmentURL));
		if (homeworkAttachmentDIR.exists()) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "DELETE FROM `HomeworkAttachment` WHERE attachmentURL = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, homeworkAttachmentURL);

				pst.executeUpdate();

				homeworkAttachmentDIR.delete();
				isDeleted = true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return isDeleted;
	}

	public boolean checkIfHomeworkFolderExist() {

		boolean isExist = false;

		File homeworkAttachmentDIR = new File(getHomeworkDIR());
		if (homeworkAttachmentDIR.exists()) {
			homeworkAttachmentDIR.mkdir();

			isExist = true;
		}

		return isExist;
	}

	public String getHomeworkDIR() {

		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
		String uploadedDir = uploadPath + File.separator + "uploadedHomeworkDIR";

		return uploadedDir;
	}

	public String getHomeworkAttachmentDIR(String homeworkAttachmentURL) {

		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
		String uploadedDir = uploadPath + File.separator + "uploadedHomeworkDIR" + File.separator
				+ homeworkAttachmentURL;

		return uploadedDir;
	}

}

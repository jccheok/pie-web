package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Attachment;
import pie.AttachmentType;
import pie.utilities.DatabaseConnector;

public class AttachmentService {

	public Attachment getAttachment(int attachmentID) {
		
		Attachment attachment = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `Attachment` WHERE attachmentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, attachmentID);
			
			resultSet = pst.executeQuery();
			
			if(resultSet.next()) {
				
				String attachmentURL = resultSet.getString("attachmentURL");
				AttachmentType attachmentTypeID = AttachmentType.getAttachmentType(resultSet.getInt("attachmentTypeID"));
				
				attachment = new Attachment(attachmentID, attachmentURL, attachmentTypeID);
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attachment;
		
	}
	
	public int createAttachment(String attachmentURL, int attachmentTypeID) {
		
		int attachmentID = -1;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "INSERT INTO `Attachment` (attachmentURL, attachmentTypeID) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, attachmentURL);
			pst.setInt(2, attachmentTypeID);
			pst.executeUpdate();

			resultSet = pst.getGeneratedKeys();
			if (resultSet.next()) {
				attachmentID = resultSet.getInt(1);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attachmentID;
	}
	
}

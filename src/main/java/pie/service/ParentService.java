package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import pie.util.DatabaseConnector;

public class ParentService {
	
	public boolean registerParent(int userID) {
		boolean registrationResult = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "INSERT INTO `Parent` (parentID) VALUES (?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userID);
			pst.executeUpdate();
			
			registrationResult = true;
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return registrationResult;
	}

}

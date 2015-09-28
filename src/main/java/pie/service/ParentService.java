package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import pie.util.DatabaseConnector;

public class ParentService {
	
	public boolean registerParent(int userID) {
		boolean registrationResult = false;
	public Parent getParent(int parentID) {
		UserService userService = new UserService();
		
		Parent parent = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Parent` WHERE parentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, parentID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				User user = userService.getUser(parentID);
				parent = new Parent(user);
				
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return parent;
	}
			pst.executeUpdate();
			
			registrationResult = true;
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return registrationResult;
	}

}

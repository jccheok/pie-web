package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.ResponseOption;
import pie.utilities.DatabaseConnector;

public class ResponseOptionService {

	public ResponseOption getResponseOption(int responseOptionID) {
		
		ResponseOption responseOption = null;
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `ResponseOption` WHERE responseOptionID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, responseOptionID);
			
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				responseOption = new ResponseOption();
				responseOption.setResponseOptionID(responseOptionID);
				responseOption.setResponseOptionText(resultSet.getString("responseOptionText"));
			}

			conn.close();
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		return responseOption;
		
	}
	
}

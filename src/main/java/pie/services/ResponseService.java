package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.ResponseOption;
import pie.ResponseQuestion;
import pie.utilities.DatabaseConnector;

public class ResponseService {
	
	public ResponseQuestion getResponseQuestion(int responseQuestionID) {
		
		ResponseQuestion responseQuestion = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `ResponseQuestion` WHERE responseQuestionID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, responseQuestionID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				responseQuestion = new ResponseQuestion();
				responseQuestion.setResponseQuestionID(responseQuestionID);
				responseQuestion.setResponseQuestionText(resultSet.getString("question"));
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseQuestion;
	}

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

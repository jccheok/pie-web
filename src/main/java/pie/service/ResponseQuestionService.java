package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.ResponseQuestion;
import pie.util.DatabaseConnector;

public class ResponseQuestionService {

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
				responseQuestion.setResponseQuestionText(resultSet
						.getString("responseQuestionText"));
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return responseQuestion;
	}
}

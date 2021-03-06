package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pie.SecurityQuestion;
import pie.utilities.DatabaseConnector;

public class SecurityQuestionService {
	
	public SecurityQuestion getSecurityQuestion(int securityQuestionID){
		
		SecurityQuestion securityQuestion = null;
		
		try{

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `SecurityQuestion` WHERE securityQuestionID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, securityQuestionID);
			
			resultSet = pst.executeQuery();
			if(resultSet.next()){
				String securityQuestionDescription = resultSet.getString("question");
				
				securityQuestion = new SecurityQuestion(securityQuestionID, securityQuestionDescription);
			}
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return securityQuestion;
	}
	
	public int getSecurityQuestionID (String securityQuestionDescription){
		
		int securityQuestionID = -1;
		
		try{
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT securityQuestionID FROM `SecurityQuestion` WHERE question = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, securityQuestionDescription);
			
			resultSet = pst.executeQuery();
			if(resultSet.next()){
				securityQuestionID = resultSet.getInt("securityQuestionID");
			}
			conn.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return securityQuestionID;
	}
	
	public String getSecurityQuestionDescription(int securityQuestionID){
		String securityQuestionDescription = null;
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT question FROM `SecurityQuestion` WHERE securityQuestionID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, securityQuestionID);
			
			resultSet = pst.executeQuery();
			if(resultSet.next()){
				securityQuestionDescription = resultSet.getString("question");
			}
			
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return securityQuestionDescription;
	}
	
	public SecurityQuestion[] getAllSecurityQuestion(){
		SecurityQuestion[] securityQuestions = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT securityQuestionID FROM `SecurityQuestion`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<SecurityQuestion> tempSecurityQuestionList = new ArrayList<SecurityQuestion>();
			while (resultSet.next()) {
				tempSecurityQuestionList.add(getSecurityQuestion(resultSet.getInt("securityQuestionID")));
			}
			securityQuestions = tempSecurityQuestionList.toArray(securityQuestions);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return securityQuestions;
	}
	

}

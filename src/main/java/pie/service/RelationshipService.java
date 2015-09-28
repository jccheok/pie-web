package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import pie.Relationship;
import pie.util.DatabaseConnector;

public class RelationshipService {

	public enum RegistrationResult {
		SUCCESS(
				"Relationship type registered."), NAME_TAKEN(
				"The relationship name you have entered is already taken!");

		private String defaultMessage;

		RegistrationResult(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}

		public String toString() {
			return this.name();
		}

		public String getDefaultMessage() {
			return defaultMessage;
		}
	}

	public int getRelationshipID(String relationshipName) {
		int relationshipID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT relationshipID FROM `Relationship` WHERE relationshipName = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, relationshipName);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				relationshipID = resultSet.getInt(1);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return relationshipID;
	}

	public Relationship getRelationship(int relationshipID) {
		Relationship relationship = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT * FROM `Relationship` WHERE relationshipID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, relationshipID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				relationship = new Relationship();
				relationship.setRelationshipID(relationshipID);
				relationship.setRelationshipName(resultSet
						.getString("relationshipName"));
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return relationship;
	}
	
	public Relationship[] getAllRelationships() {
		
		Relationship[] relationshipList = {};
		
		try {
			
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			
			String sql = "SELECT relationshipID FROM `Relationship`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<Relationship> tempRelationshipList = new ArrayList<Relationship>();
			
			while (resultSet.next()) {
				tempRelationshipList.add(getRelationship(resultSet.getInt(1)));
			}
			
			relationshipList = tempRelationshipList.toArray(relationshipList);
			
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return relationshipList;
	}

	public RegistrationResult registerRelationship(String relationshipName) {
		RegistrationResult registrationResult = RegistrationResult.SUCCESS;

		if (getRelationshipID(relationshipName) > -1) {
			registrationResult = RegistrationResult.NAME_TAKEN;
		} else {

			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `Relationship` (relationshipName) VALUES (?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, relationshipName);
				pst.executeUpdate();
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println(e);
			}

		}

		return registrationResult;
	}

}

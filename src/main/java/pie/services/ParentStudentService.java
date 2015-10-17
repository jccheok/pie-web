package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import pie.Parent;
import pie.Relationship;
import pie.Student;
import pie.constants.AddChildResult;
import pie.utilities.DatabaseConnector;

public class ParentStudentService {

	public Student[] getChildren(int parentID) {

		StudentService studentService = new StudentService();
		Student[] children = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT studentID FROM `ParentStudent` WHERE parentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, parentID);
			resultSet = pst.executeQuery();

			ArrayList<Student> tempStudents = new ArrayList<Student>();

			while (resultSet.next()) {
				tempStudents.add(studentService.getStudent(resultSet.getInt(1)));
			}

			conn.close();

			children = tempStudents.toArray(children);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return children;
	}

	public Parent[] getParents(int studentID) {

		ParentService parentService = new ParentService();
		Parent[] parents = {};

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT parentID FROM `ParentStudent` WHERE studentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			resultSet = pst.executeQuery();

			ArrayList<Parent> tempParents = new ArrayList<Parent>();

			while (resultSet.next()) {
				tempParents.add(parentService.getParent(resultSet.getInt(1)));
			}

			conn.close();

			parents = tempParents.toArray(parents);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return parents;
	}

	public Relationship getRelationship(int parentID, int studentID) {
		
		RelationshipService relationshipService = new RelationshipService();
		Relationship relationship = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT relationshipID FROM `ParentStudent` WHERE studentID = ? AND parentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, parentID);
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				relationship = relationshipService.getRelationship(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return relationship;
	}

	public Parent getMainParent(int studentID) {

		ParentService parentService = new ParentService();
		Parent mainParent = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;
			Connection conn = DatabaseConnector.getConnection();

			String sql = "SELECT parentID FROM `ParentStudent` WHERE studentID = ? AND isMainParent = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, 1);
			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				mainParent = parentService.getParent(resultSet.getInt(1));
			}

			conn.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return mainParent;
	}

	public AddChildResult addChild(int parentID, int relationshipID, String studentCode) {

		StudentService student = new StudentService();
		AddChildResult addChildResult = AddChildResult.SUCCESS;

		int studentID = student.getStudentID(studentCode);
		
		boolean isMainParent = false;
		if (getMainParent(studentID) == null) {
			isMainParent = true;
		}

		if (studentID == -1) {
			addChildResult = AddChildResult.INVALID_STUDENT_CODE;
		} else if (getRelationship(parentID, studentID) != null) {
			addChildResult = AddChildResult.CHILD_ALREADY_ADDED;
		} else {

			try {

				PreparedStatement pst = null;
				Connection conn = DatabaseConnector.getConnection();

				String sql = "INSERT INTO `ParentStudent` (parentID, studentID, relationshipID, isMainParent) VALUES (?, ?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, parentID);
				pst.setInt(2, studentID);
				pst.setInt(3, relationshipID);
				pst.setInt(4, isMainParent ? 1:0);

				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return addChildResult;
	}
}

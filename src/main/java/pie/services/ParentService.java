package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import pie.Parent;
import pie.Student;
import pie.User;
import pie.UserType;
import pie.constants.AddChildResult;
import pie.constants.UserRegistrationResult;
import pie.utilities.DatabaseConnector;

public class ParentService {

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

	public UserRegistrationResult registerParent(String userFirstName, String userLastName, String userEmail,
			String userPassword, String userMobile) {

		UserService userService = new UserService();
		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else {
			
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName, userEmail, userPassword, userMobile) VALUES (?, ?, ?, ?, ?, ?)";
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, UserType.PARENT.getUserTypeID());
				pst.setString(2, userFirstName);
				pst.setString(3, userLastName);
				pst.setString(4, userEmail);
				pst.setString(5, userPassword);
				pst.setString(6, userMobile);
				pst.executeUpdate();

				resultSet = pst.getGeneratedKeys();

				if (resultSet.next()) {

					int newUserID = resultSet.getInt(1);

					sql = "INSERT INTO `Parent` (parentID) VALUES (?)";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, newUserID);
					pst.executeUpdate();

				}

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return registrationResult;
	}
	
	public Student[] getChildren(int parentID){
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

			while(resultSet.next()) {
				tempStudents.add(new StudentService().getStudent((resultSet.getInt("studentID"))));
			}

			conn.close();
			
			children = tempStudents.toArray(children);

		} catch (Exception e) {

			System.out.println(e);
		}

		
		return children;
	}
	
	
	public boolean hasChild(int studentID, int parentID){
		boolean hasChild = false;
		Student[] children = getChildren(parentID);
		
		for(Student student : children){
			if(studentID == student.getUserID()){
				hasChild = true;
				break;
			}
		}
		return hasChild;
	}
	
	
	public AddChildResult addChild(int parentID, int relationshipID, String studentCode){
		AddChildResult addChildResult = AddChildResult.SUCCESS;
		StudentService student = new StudentService();
		int studentID = student.getStudentID(studentCode);
		
		if(studentID != -1){
			addChildResult = AddChildResult.WRONG_STUDENT_CODE;
			if(!hasChild(studentID, parentID)){
				try {

					PreparedStatement pst = null;
					Connection conn = DatabaseConnector.getConnection();

					String sql = "INSERT INTO `ParentStudent` (parentID, studentID, relationshipID) VALUES (?, ?, ?)";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, parentID);
					pst.setInt(2, studentID);
					pst.setInt(3, relationshipID);

					pst.executeUpdate();
					
					conn.close();

				} catch (Exception e) {

					System.out.println(e);
				}
			}
			else{
				addChildResult = AddChildResult.CHILD_ALREADY_ADDED;
			}
		}
		
		return addChildResult;
	}

}

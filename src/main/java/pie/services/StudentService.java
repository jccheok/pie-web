package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import pie.Group;
import pie.School;
import pie.Student;
import pie.User;
import pie.UserType;
import pie.constants.JoinGroupResult;
import pie.constants.UserRegistrationResult;
import pie.utilities.DatabaseConnector;
import pie.utilities.Utilities;

public class StudentService {

	public boolean isAvailableStudentCode(String studentCode) {
		
		boolean isAvailable = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `Student` WHERE studentCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			isAvailable = !pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isAvailable;
	}

	public boolean isRegisteredStudent(String studentCode) {
		
		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isVerified FROM `Student`,`User` WHERE `Student`.studentID = `User`.userID AND studentCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			resultSet = pst.executeQuery();
			if (resultSet.next()) {

				isRegistered = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isRegistered;
	}

	public boolean isMember(Student student, Group group) {
		
		boolean isMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StudentGroup` WHERE studentID = ? AND groupID = ? AND studentGroupIsValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, student.getUserID());
			pst.setInt(2, group.getGroupID());
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return isMember;

	}

	public boolean enlistStudent(String userFirstName, String userLastName, String studentCode, int schoolID,
			int groupID, int studentGroupIndexNumber) {

		GroupService groupService = new GroupService();
		boolean enlistResult = false;

		if (isAvailableStudentCode(studentCode)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;
				ResultSet resultSet = null;

				String sql = "INSERT INTO `User` (userTypeID, userFirstName, userLastName) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				pst.setInt(1, UserType.STUDENT.getUserTypeID());
				pst.setString(2, userFirstName);
				pst.setString(3, userLastName);
				pst.executeUpdate();

				resultSet = pst.getGeneratedKeys();

				if (resultSet.next()) {

					int newStudentID = resultSet.getInt(1);
					sql = "INSERT INTO `Student` (studentID, schoolID, studentCode) VALUES (?, ?, ?)";
					pst = conn.prepareStatement(sql);
					pst.setInt(1, newStudentID);
					pst.setInt(2, schoolID);
					pst.setString(3, studentCode);
					pst.executeUpdate();

					enlistResult = groupService.addStudentToGroup(groupID,
							newStudentID, studentGroupIndexNumber);

				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return enlistResult;
	}

	public Student getStudent(int studentID) {

		SchoolService schoolService = new SchoolService();
		UserService userService = new UserService();
		Student student = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Student` WHERE studentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				User user = userService.getUser(studentID);
				School studentSchool = schoolService.getSchool(resultSet
						.getInt("schoolID"));
				String studentCode = resultSet.getString("studentCode");
				Date studentEnlistmentDate = new Date(resultSet.getTimestamp(
						"studentEnlistmentDate").getTime());

				student = new Student(user, studentSchool, studentCode,
						studentEnlistmentDate);
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return student;
	}

	public UserRegistrationResult registerStudent(String userEmail, String userPassword, String userMobile,
			String studentCode) {

		UserService userService = new UserService();
		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else if (isAvailableStudentCode(studentCode)){
			registrationResult = UserRegistrationResult.INVALID_STUDENT_CODE;
		} else if (isRegisteredStudent(studentCode)) {
			registrationResult = UserRegistrationResult.STUDENT_REGISTERED;
		} else {
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `User`,`Student` SET userRegistrationDate = NOW(), userLastUpdate = NOW(), userEmail = ?, userPassword = ?, userMobile = ? WHERE `User`.userID = `Student`.studentID AND studentCode = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);
				pst.setString(3, userMobile);
				pst.setString(4, studentCode);
				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return registrationResult;
	}

	public JoinGroupResult joinGroup(int studentID, int groupID, String groupCode) {

		GroupService groupService = new GroupService();
		JoinGroupResult joinGroupResult = JoinGroupResult.SUCCESS;

		Group group = groupService.getGroup(groupID);
		
		if (group == null || !group.groupIsValid()) {
			joinGroupResult = JoinGroupResult.INVALID_GROUP;
		} else if (!group.groupIsOpen()) {
			joinGroupResult = JoinGroupResult.GROUP_CLOSED;
		} else if (group.getGroupCode() != null && groupCode == null) {
			joinGroupResult = JoinGroupResult.MISSING_GROUP_CODE;
		} else if (!group.getGroupCode().equals(groupCode)) {
			joinGroupResult = JoinGroupResult.INVALID_GROUP_CODE;
		} else {
			int nextStudentIndexNumber = groupService.getNextStudentIndexNumber(groupID);
			groupService.addStudentToGroup(groupID, studentID, nextStudentIndexNumber);
		}
		
		return joinGroupResult;
	}
	
	public String generateStudentCode() {
		
		String newStudentCode = Utilities.generateString(5);
		while (!isAvailableStudentCode(newStudentCode)) {
			newStudentCode = Utilities.generateString(5);
		}
		
		return newStudentCode;
	}
	
	
	public int getStudentID(String studentCode){
		int studentID = -1;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT studentID FROM `Student` WHERE studentCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				
				studentID = resultSet.getInt("studentID");
				
			}

			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		
		return studentID;
	}

}

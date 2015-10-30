package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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

			String sql = "SELECT * FROM `Student` WHERE code = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			isAvailable = !pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isAvailable;
	}

	public boolean isRegisteredStudent(String studentCode) {

		boolean isRegistered = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT isVerified FROM `Student`,`User` WHERE `Student`.studentID = `User`.userID AND code = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			resultSet = pst.executeQuery();
			if (resultSet.next()) {

				isRegistered = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isRegistered;
	}

	public boolean isMember(int studentID, int groupID) {

		boolean isMember = false;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;

			String sql = "SELECT * FROM `StudentGroup` WHERE studentID = ? AND groupID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);

			isMember = pst.executeQuery().next();

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isMember;

	}

	public boolean enlistStudent(JSONArray studentList, int groupID) {
		
		boolean enlistResult = false;
		StudentGroupService studentGroupService = new StudentGroupService();
		GroupService groupService = new GroupService();
		
		String userFirstName = null;
		String userLastName = null;
		String studentCode = null;
		int studentGroupIndexNumber = 0;
		String SUID = null;
		int schoolID = groupService.getGroup(groupID).getSchool().getSchoolID();
		
		Connection conn = DatabaseConnector.getConnection();
		Savepoint dbSavepoint = null;
		try {
			
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			conn.setAutoCommit(false);
			dbSavepoint = conn.setSavepoint("dbSavepoint");
			JSONArray tempArray = new JSONArray();
			JSONArray newStudents = new JSONArray();

			String sql = "INSERT INTO `User` (userTypeID, firstName, lastName) VALUES (?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			for (int index = 0; index < studentList.length(); index++) {
				
				JSONObject studentObject = studentList.getJSONObject(index);
				userFirstName = studentObject.getString("studentFirstName");
				userLastName = studentObject.getString("studentLastName");
				studentGroupIndexNumber = studentObject.getInt("studentGroupIndexNumber");
				SUID = studentObject.getString("SUID");
				studentCode = generateStudentCode();
				
				if(studentExists(SUID) == null){
					pst.setInt(1, UserType.STUDENT.getUserTypeID());
					pst.setString(2, userFirstName);
					pst.setString(3, userLastName);
					pst.addBatch();
				}else{
					tempArray.put(studentObject);
					studentList.remove(index);
				}
			
			}
			
			pst.executeBatch();
			resultSet = pst.getGeneratedKeys();
			
			pst.clearBatch();
			
			sql = "INSERT INTO `Student` (studentID, schoolID, code, SUID) VALUES (?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			
			while(resultSet.next()){
				
				int newStudentID = resultSet.getInt(1);
				studentCode = generateStudentCode();

				pst.setInt(1, newStudentID);
				pst.setInt(2, schoolID);
				pst.setString(3, studentCode);
				pst.setString(4, SUID);
				pst.addBatch();
				
			}
			
			pst.executeBatch();
			pst.clearBatch();
			dbSavepoint = conn.setSavepoint("dbSavepoint2");
			for(int i = 0; i < tempArray.length(); i++){
				JSONObject tempObject = tempArray.getJSONObject(i);
				Student student = studentExists(tempObject.getString("SUID"));
				
				if(student != null){
					JSONObject studentItem = new JSONObject();
					studentItem.put("studentID", student.getUserID());
					studentItem.put("studentGroupIndexNumber", tempObject.getInt("studentGroupIndexNumber"));
					newStudents.put(studentItem);
				}
				
			}
			
			while(resultSet.next()){
				
				int newStudentID = resultSet.getInt(1);
				Student student = getStudent(newStudentID);
				
				for(int i = 0; i < studentList.length(); i++){
					
					JSONObject studentObject = studentList.getJSONObject(i);

					if(student.getSUID().equals(studentObject.getString("SUID"))){
						studentGroupIndexNumber = studentObject.getInt("studentGroupIndexNumber");
						break;
					}
				}
				
				JSONObject studentItem = new JSONObject();
				studentItem.put("studentID", newStudentID);
				studentItem.put("studentGroupIndexNumber", studentGroupIndexNumber);
				newStudents.put(studentItem);
				
			}
			if(studentGroupService.addStudentToGroup(groupID, newStudents)){
				conn.commit();
				enlistResult = true;
				conn.close();
			}else{
				conn.rollback(dbSavepoint);
			}

		} catch (SQLException e) {
			try {
				conn.rollback(dbSavepoint);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return enlistResult;
	}

	public Group[] getJoinedGroups(int studentID) {

		GroupService groupService = new GroupService();
		Group[] joinedGroups = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT `StudentGroup`.groupID FROM `StudentGroup`,`Group` WHERE `StudentGroup`.groupID = `Group`.groupID AND `Group`.isValid = ? AND `StudentGroup`.isValid = ? AND studentID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, studentID);

			resultSet = pst.executeQuery();

			List<Group> tempJoinedGroups = new ArrayList<Group>();
			while (resultSet.next()) {
				tempJoinedGroups.add(groupService.getGroup(resultSet.getInt(1)));
			}
			joinedGroups = tempJoinedGroups.toArray(joinedGroups);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return joinedGroups;
	}

	public int getStudentGroupIndexNumber(int groupID, int studentID) {
		int studentIndexNumber = -1;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT indexNumber FROM `StudentGroup` WHERE groupID = ? AND studentID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			pst.setInt(2, studentID);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				studentIndexNumber = resultSet.getInt(1);

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentIndexNumber;
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
				School studentSchool = schoolService.getSchool(resultSet.getInt("schoolID"));
				String studentCode = resultSet.getString("code");
				Date studentEnlistmentDate = new Date(resultSet.getTimestamp("enlistmentDate").getTime());
				String SUID = resultSet.getString("SUID");

				student = new Student(user, studentSchool, studentCode, studentEnlistmentDate, SUID);
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return student;
	}

	public UserRegistrationResult registerStudent(String userEmail, String userPassword, String userMobile,
			String studentCode, int securityQuestionID, String securityQuestionAnswer) {

		UserService userService = new UserService();
		UserRegistrationResult registrationResult = UserRegistrationResult.SUCCESS;

		if (userService.isRegisteredUser(userEmail)) {
			registrationResult = UserRegistrationResult.EMAIL_TAKEN;
		} else if (isAvailableStudentCode(studentCode)) {
			registrationResult = UserRegistrationResult.INVALID_STUDENT_CODE;
		} else if (isRegisteredStudent(studentCode)) {
			registrationResult = UserRegistrationResult.STUDENT_REGISTERED;
		} else {
			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `User`,`Student` SET registrationDate = NOW(), lastUpdate = NOW(), emailAddress = ?, password = ?, mobile = ?, securityQuestionID = ?, securityQuestionAnswer = ?, passwordLastUpdate = NOW() WHERE `User`.userID = `Student`.studentID AND code = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, userEmail);
				pst.setString(2, userPassword);
				pst.setString(3, userMobile);
				pst.setInt(4, securityQuestionID);
				pst.setString(5, securityQuestionAnswer);
				pst.setString(6, studentCode);
				pst.executeUpdate();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return registrationResult;
	}

	public JoinGroupResult joinGroup(int studentID, int groupID, String groupCode) {
		
		GroupService groupService = new GroupService();
		StudentGroupService studentGroupService = new StudentGroupService();

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
		} else if (groupService.hasGroupMember(groupID, studentID)) {
			joinGroupResult = JoinGroupResult.ALREADY_MEMBER;
		} else {
			int nextStudentIndexNumber = groupService.getNextStudentIndexNumber(groupID);
			JSONArray studentList = new JSONArray();
			JSONObject student = new JSONObject();
			student.put("studentID", studentID);
			student.put("studentGroupIndexNumber", nextStudentIndexNumber);
			studentList.put(student);
			studentGroupService.addStudentToGroup(groupID, studentList);
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

	public int getStudentID(String studentCode) {
		int studentID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT studentID FROM `Student` WHERE code = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, studentCode);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				studentID = resultSet.getInt("studentID");

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentID;
	}
	
	public Student studentExists (String SUID){
		Student student = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT studentID FROM `Student` WHERE SUID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, SUID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				student = getStudent(resultSet.getInt("studentID"));

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return student;
	}
}

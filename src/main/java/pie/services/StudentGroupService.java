package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import pie.Group;
import pie.Student;
import pie.StudentGroup;
import pie.constants.LeaveGroupResult;
import pie.utilities.DatabaseConnector;

public class StudentGroupService {

	public boolean hasGroupMember(int groupID, int studentID) {
		
		boolean hasMember = false;
		
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT studentID from `StudentGroup` where studentID = ? AND groupID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, groupID);
			pst.setInt(3, 1);
			resultSet = pst.executeQuery();
					
			if (resultSet.next()) {
				hasMember = resultSet.getInt(1) == 1;
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasMember;
	}
	
	public StudentGroup getStudentGroup(int groupID, int studentID){
		StudentGroup studentGroup = null;
		StudentService studentService = new StudentService();
		GroupService groupService = new GroupService();
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;
			String sql = "SELECT * FROM `StudentGroup` WHERE studentID = ? AND groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, studentID);
			pst.setInt(2, groupID);
			
			resultSet = pst.executeQuery();
			
			if(resultSet.next()){
				int studentGroupID = resultSet.getInt("studentGroupID");
				Student student = studentService.getStudent(studentID);
				Group group = groupService.getGroup(studentGroupID);
				int indexNumber = resultSet.getInt("indexNumber");
				Date leaveDate = new Date(resultSet.getTimestamp("leaveDate").getTime());
				Date joinDate = new Date(resultSet.getTimestamp("joinDate").getTime());
				boolean isValid = resultSet.getInt("isValid") == 1 ? true: false;
				
				studentGroup = new StudentGroup(studentGroupID, student, group, indexNumber, joinDate, leaveDate, isValid);
			}
			
			conn.close();
			
		}catch(Exception e){
			
		}

		return studentGroup;
	}
	
	public boolean addStudentToGroup(int groupID, int studentID, int studentGroupIndexNumber) {
		
		StudentService studentService = new StudentService();
		boolean addResult = false;

		if (!studentService.isMember(studentID, groupID)) {

			try {

				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "INSERT INTO `StudentGroup` (groupID, studentID, indexNumber) VALUES (?, ?, ?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, groupID);
				pst.setInt(2, studentID);
				pst.setInt(3, studentGroupIndexNumber);
				pst.executeUpdate();

				addResult = true;

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return addResult;
	}
	
	public Student[] getStudentMembers(int groupID) {

		StudentService studentService = new StudentService();
		Student[] studentMembers = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT studentID FROM `StudentGroup` WHERE groupID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			resultSet = pst.executeQuery();

			ArrayList<Student> tempStudentMembers = new ArrayList<Student>();
			while (resultSet.next()) {
				tempStudentMembers.add(studentService.getStudent(resultSet.getInt("studentID")));
			}
			studentMembers = tempStudentMembers.toArray(studentMembers);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentMembers;
	}
	
	public Date getStudentGroupJoinDate(int groupID, int studentID) {

		Date studentGroupJoinDate = null;

		try {
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT joinDate FROM `StudentGroup` WHERE groupID = ? AND studentID = ? AND isValid = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, groupID);
			pst.setInt(2, studentID);
			pst.setInt(3, 1);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {

				studentGroupJoinDate = new Date(resultSet.getTimestamp(1).getTime());

			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentGroupJoinDate;
	}
	
	public boolean removeStudentFromGroup(int groupID, int studentID) {
		boolean removeResult = false;
		
		if (hasGroupMember(groupID, studentID)) {
			try {
				Connection conn = DatabaseConnector.getConnection();
				PreparedStatement pst = null;

				String sql = "UPDATE `StudentGroup` SET isValid = ? WHERE groupID = ? AND studentID = ?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, 0);
				pst.setInt(2, groupID);
				pst.setInt(3, studentID);

				pst.executeUpdate();
				
				removeResult = true;
				
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return removeResult;
	}
	
	public LeaveGroupResult leaveGroup(int groupID, int studentID) {
		LeaveGroupResult leaveGroupResult = LeaveGroupResult.SUCCESS;

		if (!hasGroupMember(studentID, groupID)) {
			leaveGroupResult = LeaveGroupResult.NOT_MEMBER;
		} else {
			removeStudentFromGroup(groupID, studentID);
		}

		return leaveGroupResult;
	}
}

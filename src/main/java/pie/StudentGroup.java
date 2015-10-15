package pie;

import java.util.Date;

public class StudentGroup {

	private int studentGroupID;
	private Student student;
	private Group group;
	private int indexNumber;
	private Date joinDate;
	private Date leaveDate;
	private boolean isValid;
	
	
	public StudentGroup(int studentGroupID, Student student, Group group, int indexNumber, Date joinDate, Date leaveDate,
			boolean isValid) {
		setStudentGroupID(studentGroupID);
		setStudent(student);		
		setGroup(group);
		setIndexNumber(indexNumber);
		setJoinDate(joinDate);
		setLeaveDate(leaveDate);
		setValid(isValid);
	}
	
	public int getStudentGroupID() {
		return studentGroupID;
	}
	public void setStudentGroupID(int studentGroupID) {
		this.studentGroupID = studentGroupID;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Group getGroupID() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public int getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
}

package pie;

import java.util.Date;

public class Student extends User {

	private School school;
	private String studentCode;
	private Date studentEnlistmentDate;
	
	public Student() {

	}

	public Student(User user, School studentSchool, String studentCode,
			Date studentEnlistmentDate) {
		super(user);
		setStudentCode(studentCode);
		setSchool(studentSchool);
		setStudentEnlistmentDate(studentEnlistmentDate);
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public Date getStudentEnlistmentDate() {
		return studentEnlistmentDate;
	}

	public void setStudentEnlistmentDate(Date studentEnlistmentDate) {
		this.studentEnlistmentDate = studentEnlistmentDate;
	}

}

package pie;

import java.util.Date;

public class Student extends User {

	private School school;
	private String studentCode;
	private Date studentEnlistmentDate;
	private String SUID;
	
	public Student() {

	}

	public Student(User user, School studentSchool, String studentCode,
			Date studentEnlistmentDate, String SUID) {
		super(user);
		setStudentCode(studentCode);
		setSchool(studentSchool);
		setStudentEnlistmentDate(studentEnlistmentDate);
		setSUID(SUID);
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

	public String getSUID() {
		return SUID;
	}

	public void setSUID(String SUID) {
		SUID = SUID;
	}

}

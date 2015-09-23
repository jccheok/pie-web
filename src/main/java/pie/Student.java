package pie;

public class Student extends User {

	private School school;
	private String studentCode;

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
	
}

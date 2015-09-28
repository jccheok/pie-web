package pie;

public class Staff extends User {

	private String staffTitle;
	private School school;
	private boolean staffIsSchoolAdmin;

	public Staff() {}

	public Staff(User user, School staffSchool, String staffTitle,
			boolean staffIsSchoolAdmin) {
		super(user);
		setStaffTitle(staffTitle);
		setSchool(staffSchool);
		setStaffIsSchoolAdmin(staffIsSchoolAdmin);
	}

	public String getStaffTitle() {
		return staffTitle;
	}

	public void setStaffTitle(String staffTitle) {
		this.staffTitle = staffTitle;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public boolean staffIsSchoolAdmin() {
		return staffIsSchoolAdmin;
	}

	public void setStaffIsSchoolAdmin(boolean staffIsSchoolAdmin) {
		this.staffIsSchoolAdmin = staffIsSchoolAdmin;
	}

}

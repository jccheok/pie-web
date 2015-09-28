package pie;

public class TeacherRole {

	private int teacherRoleID;
	private String teacherRoleName;
	private boolean teacherRoleIsAdmin;
	private boolean teacherRoleIsOwner;
	private boolean teacherRoleIsDefault;

	public int getTeacherRoleID() {
		return teacherRoleID;
	}

	public void setTeacherRoleID(int teacherRoleID) {
		this.teacherRoleID = teacherRoleID;
	}

	public String getTeacherRoleName() {
		return teacherRoleName;
	}

	public void setTeacherRoleName(String teacherRoleName) {
		this.teacherRoleName = teacherRoleName;
	}

	public boolean teacherRoleIsAdmin() {
		return teacherRoleIsAdmin;
	}

	public void setTeacherRoleIsAdmin(boolean teacherRoleIsAdmin) {
		this.teacherRoleIsAdmin = teacherRoleIsAdmin;
	}

	public boolean teacherRoleIsOwner() {
		return teacherRoleIsOwner;
	}

	public void setTeacherRoleIsOwner(boolean teacherRoleIsOwner) {
		this.teacherRoleIsOwner = teacherRoleIsOwner;
	}

	public boolean isTeacherRoleIsDefault() {
		return teacherRoleIsDefault;
	}

	public void setTeacherRoleIsDefault(boolean teacherRoleIsDefault) {
		this.teacherRoleIsDefault = teacherRoleIsDefault;
	}
	
	public boolean equals(TeacherRole teacherRole) {
		return teacherRoleID == teacherRole.getTeacherRoleID();
	}
}

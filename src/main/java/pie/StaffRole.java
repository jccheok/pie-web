package pie;

public class StaffRole {

	private int staffRoleID;
	private String staffRoleName;
	private boolean staffRoleIsAdmin;
	private boolean staffRoleIsOwner;
	private boolean staffRoleIsDefault;

	public int getStaffRoleID() {
		return staffRoleID;
	}

	public void setStaffRoleID(int staffRoleID) {
		this.staffRoleID = staffRoleID;
	}

	public String getStaffRoleName() {
		return staffRoleName;
	}

	public void setStaffRoleName(String staffRoleName) {
		this.staffRoleName = staffRoleName;
	}

	public boolean staffRoleIsAdmin() {
		return staffRoleIsAdmin;
	}

	public void setStaffRoleIsAdmin(boolean staffRoleIsAdmin) {
		this.staffRoleIsAdmin = staffRoleIsAdmin;
	}

	public boolean staffRoleIsOwner() {
		return staffRoleIsOwner;
	}

	public void setStaffRoleIsOwner(boolean staffRoleIsOwner) {
		this.staffRoleIsOwner = staffRoleIsOwner;
	}

	public boolean staffRoleIsDefault() {
		return staffRoleIsDefault;
	}

	public void setStaffRoleIsDefault(boolean staffRoleIsDefault) {
		this.staffRoleIsDefault = staffRoleIsDefault;
	}

	public boolean equals(StaffRole staffRole) {
		return staffRoleID == staffRole.getStaffRoleID();
	}
}

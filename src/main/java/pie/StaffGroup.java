package pie;

import java.util.Date;

public class StaffGroup {

	private int staffGroupID;
	private Group group;
	private Staff staff;
	private StaffRole staffRole;
	private Date joinDate;
	private Date leaveDate;
	private boolean isValid;
	
	
	
	public StaffGroup(int staffGroupID, Group group, Staff staff, StaffRole staffRole, Date joinDate, Date leaveDate,
			boolean isValid) {

		setStaff(staff);
		setGroup(group);
		setStaffRole(staffRole);
		setJoinDate(joinDate);
		setLeaveDate(leaveDate);
		setStaffGroupID(staffGroupID);
		setValid(isValid);
		
	}
	
	public int getStaffGroupID() {
		return staffGroupID;
	}
	public void setStaffGroupID(int staffGroupID) {
		this.staffGroupID = staffGroupID;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public StaffRole getStaffRole() {
		return staffRole;
	}
	public void setStaffRole(StaffRole staffRole) {
		this.staffRole = staffRole;
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

package pie;

import java.util.Date;

public class Group {

	private int groupID;
	private School school;
	private String groupName;
	private String groupDescription;
	private int groupMaxDailyHomeworkMinutes;
	private GroupType groupType;
	private String groupCode;
	private boolean groupIsOpen;
	private Date groupLastUpdate;
	private Date groupDateCreated;
	
	public int getGroupID() {
		return groupID;
	}
	
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	public School getSchool() {
		return school;
	}
	
	public void setSchool(School school) {
		this.school = school;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupDescription() {
		return groupDescription;
	}
	
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	
	public int getGroupMaxDailyHomeworkMinutes() {
		return groupMaxDailyHomeworkMinutes;
	}
	
	public void setGroupMaxDailyHomeworkMinutes(int groupMaxDailyHomeworkMinutes) {
		this.groupMaxDailyHomeworkMinutes = groupMaxDailyHomeworkMinutes;
	}
	
	public GroupType getGroupType() {
		return groupType;
	}
	
	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}
	
	public String getGroupCode() {
		return groupCode;
	}
	
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	public boolean isGroupIsOpen() {
		return groupIsOpen;
	}
	
	public void setGroupIsOpen(boolean groupIsOpen) {
		this.groupIsOpen = groupIsOpen;
	}
	
	public Date getGroupLastUpdate() {
		return groupLastUpdate;
	}
	
	public void setGroupLastUpdate(Date groupLastUpdate) {
		this.groupLastUpdate = groupLastUpdate;
	}
	
	public Date getGroupDateCreated() {
		return groupDateCreated;
	}
	
	public void setGroupDateCreated(Date groupDateCreated) {
		this.groupDateCreated = groupDateCreated;
	}
	
	
	
	
}

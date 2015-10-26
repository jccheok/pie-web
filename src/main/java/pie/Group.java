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
	private Date groupDateDeleted;
	private boolean groupIsValid;
	private Date expiryDate;

	public Group() {
		
	}

	public Group(int groupID, School school, String groupName,
			String groupDescription, int groupMaxDailyHomeworkMinutes,
			GroupType groupType, String groupCode, boolean groupIsOpen,
			boolean groupIsValid, Date groupLastUpdate, Date groupDateCreated, Date expiryDate) {
		this.groupID = groupID;
		this.school = school;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.groupMaxDailyHomeworkMinutes = groupMaxDailyHomeworkMinutes;
		this.groupType = groupType;
		this.groupCode = groupCode;
		this.groupIsOpen = groupIsOpen;
		this.groupIsValid = groupIsValid;
		this.groupLastUpdate = groupLastUpdate;
		this.groupDateCreated = groupDateCreated;
		this.expiryDate = expiryDate;
	}

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

	public boolean groupIsOpen() {
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

	public Date getGroupDateDeleted() {
		return groupDateDeleted;
	}

	public void setGroupDateDeleted(Date groupDateDeleted) {
		this.groupDateDeleted = groupDateDeleted;
	}

	public boolean groupIsValid() {
		return groupIsValid;
	}

	public void setGroupIsValid(boolean groupIsValid) {
		this.groupIsValid = groupIsValid;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}

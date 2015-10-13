package pie;

public enum GroupType {

	HOME(1), CCA(2), SUBJECT(3), LEVEL(4);
	
	private int groupTypeID;
	
	GroupType(int userTypeID) {
		this.groupTypeID = userTypeID;
	}
	
	public int getGroupTypeID() {
		return groupTypeID;
	}

	public static GroupType getGroupType(int groupTypeID) {
		
		for(GroupType groupType: GroupType.values()) {
			if (groupTypeID == groupType.getGroupTypeID()) {
				return groupType;
			}
		}
		
		return null;
	}
	
	public String toString() {
		return this.name();
	}
}

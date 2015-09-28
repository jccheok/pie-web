package pie;

public enum GroupType {

	CLASS, CCA;

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

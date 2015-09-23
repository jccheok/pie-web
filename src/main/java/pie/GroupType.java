package pie;

public enum GroupType {

	CLASS, CCA;

	public static GroupType getGroupType(int groupTypeID) {
		GroupType groupType = null;

		switch (groupTypeID) {
		case 1:
			groupType = CLASS;
			break;
		case 2:
			groupType = CCA;
			break;
		}

		return groupType;
	}
}

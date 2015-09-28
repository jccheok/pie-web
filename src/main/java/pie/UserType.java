package pie;

public enum UserType {

	STAFF(1), PARENT(2), STUDENT(3), ADMIN(4);
	
	private int userTypeID;
	
	UserType(int userTypeID) {
		this.userTypeID = userTypeID;
	}
	
	public int getUserTypeID() {
		return userTypeID;
	}

	public static UserType getUserType(int userTypeID) {
		
		for(UserType userType: UserType.values()) {
			if (userTypeID == userType.getUserTypeID()) {
				return userType;
			}
		}
		
		return null;
	}
	
	public String toString() {
		return this.name();
	}
}

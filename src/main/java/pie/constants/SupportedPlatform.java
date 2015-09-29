package pie.constants;

import java.util.Arrays;

import pie.UserType;

public enum SupportedPlatform {

	WEB(1, new UserType[] { UserType.ADMIN, UserType.STAFF }),
	MOBILE(2, new UserType[] { UserType.PARENT, UserType.STUDENT });

	private int supportedPlatformID;
	private UserType[] supportedUserTypes;

	SupportedPlatform(int supportedPlatformID, UserType[] supportedUserTypes) {
		setSupportedPlatformID(supportedPlatformID);
		setSupportedUserTypes(supportedUserTypes);
	}

	public int getSupportedPlatformID() {
		return supportedPlatformID;
	}

	public void setSupportedPlatformID(int supportedPlatformID) {
		this.supportedPlatformID = supportedPlatformID;
	}

	public UserType[] getSupportedUserTypes() {
		return supportedUserTypes;
	}

	public void setSupportedUserTypes(UserType[] supportedUserTypes) {
		this.supportedUserTypes = supportedUserTypes;
	}

	public boolean supportsUserType(UserType userType) {
		return Arrays.asList(supportedUserTypes).contains(userType);
	}

	public static SupportedPlatform getSupportedPlatform(int supportedPlatformID) {

		for (SupportedPlatform supportedPlatform : SupportedPlatform.values()) {
			if (supportedPlatformID == supportedPlatform.getSupportedPlatformID()) {
				return supportedPlatform;
			}
		}

		return null;
	}
}

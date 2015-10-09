package pie;

public enum AttachmentType {

	JPG(1), NONE(2), PDF(3), PNG(4);

	private int attachmentTypeID;

	AttachmentType(int attachmentTypeID) {
		this.attachmentTypeID = attachmentTypeID;
	}

	public int getAttachmentTypeID() {
		return attachmentTypeID;
	}

	public static AttachmentType getAttachmentType(int attachmentTypeID) {

		for(AttachmentType attachmentType: AttachmentType.values()) {
			if (attachmentTypeID == attachmentType.getAttachmentTypeID()) {
				return attachmentType;
			}
		}

		return null;
	}

	public String toString() {
		return this.name();
	}
}

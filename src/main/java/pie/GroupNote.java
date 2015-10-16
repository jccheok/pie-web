package pie;

import java.util.Date;

public class GroupNote {

	private int groupNoteID;
	private Note note;
	private Group group;
	private Staff staff;
	private Date publishDate;
	
	public GroupNote(int groupNoteID, Note note, Group group, Staff staff,
			Date publishDate) {
		
		setGroupNoteID(groupNoteID);
		setNote(note);
		setGroup(group);
		setStaff(staff);
		setPublishDate(publishDate);
	}

	public int getGroupNoteID() {
		return groupNoteID;
	}
	
	public void setGroupNoteID(int groupNoteID) {
		this.groupNoteID = groupNoteID;
	}
	
	public Note getNote() {
		return note;
	}
	
	public void setNote(Note note) {
		this.note = note;
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
	
	public Date getPublishDate() {
		return publishDate;
	}
	
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}

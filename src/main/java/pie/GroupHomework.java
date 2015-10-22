package pie;

import java.util.Date;

public class GroupHomework {

	private int groupHomeworkID;
	private Group group;
	private Homework homework;
	private Staff publisher;
	private int markingEffort;
	private Date actualMarkingCompletionDate;
	private Date targetMarkingCompletionDate;
	private Date dueDate;
	private Date publishDate;
	private boolean isDraft;
	private boolean isGraded;
	private boolean isDeleted;

	public GroupHomework(int groupHomeworkID, Group group, Homework homework, Staff publisher, int markingEffort,
			Date actualMarkingCompletionDate, Date targetMarkingCompletionDate, Date dueDate, Date publishDate,
			boolean isDraft, boolean isGraded, boolean isDeleted) {
		// TODO Auto-generated constructor stub
		setGroupHomeworkID(groupHomeworkID);
		setGroup(group);
		setHomework(homework);
		setPublisher(publisher);
		setMarkingEffort(markingEffort);
		setActualMarkingCompletionDate(actualMarkingCompletionDate);
		setTargetMarkingCompletionDate(targetMarkingCompletionDate);
		setPublishDate(publishDate);
		setDueDate(dueDate);
		setDraft(isDraft);
		setGraded(isGraded);
		setDeleted(isDeleted);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Homework getHomework() {
		return homework;
	}

	public void setHomework(Homework homework) {
		this.homework = homework;
	}

	public Staff getPublisher() {
		return publisher;
	}

	public void setPublisher(Staff publisher) {
		this.publisher = publisher;
	}

	public int getGroupHomeworkID() {
		return groupHomeworkID;
	}

	public void setGroupHomeworkID(int groupHomeworkID) {
		this.groupHomeworkID = groupHomeworkID;
	}


	public int getMarkingEffort() {
		return markingEffort;
	}

	public void setMarkingEffort(int markingEffort) {
		this.markingEffort = markingEffort;
	}

	public Date getActualMarkingCompletionDate() {
		return actualMarkingCompletionDate;
	}

	public void setActualMarkingCompletionDate(Date actualMarkingCompletionDate) {
		this.actualMarkingCompletionDate = actualMarkingCompletionDate;
	}

	public Date getTargetMarkingCompletionDate() {
		return targetMarkingCompletionDate;
	}

	public void setTargetMarkingCompletionDate(Date targetMarkingCompletionDate) {
		this.targetMarkingCompletionDate = targetMarkingCompletionDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isDraft() {
		return isDraft;
	}

	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public boolean isGraded() {
		return isGraded;
	}

	public void setGraded(boolean isGraded) {
		this.isGraded = isGraded;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}

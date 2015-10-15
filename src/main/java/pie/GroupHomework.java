package pie;

import java.util.Date;

public class GroupHomework {

	private int groupHomeworkID;
	private int groupID;
	private int homeworkID;
	private int publisherID;
	private int markingEffort;
	private Date actualMarkingCompletionDate;
	private Date targetMarkingCompletionDate;
	private Date dueDate;
	private Date publishDate;
	private boolean isDraft;
	private boolean isGraded;

	public GroupHomework(int groupHomeworkID, int groupID, int homeworkID, int publisherID, int markingEffort,
			Date actualMarkingCompletionDate, Date targetMarkingCompletionDate, Date dueDate, Date publishDate,
			boolean isDraft, boolean isGraded) {
		// TODO Auto-generated constructor stub
		setGroupHomeworkID(groupHomeworkID);
		setGroupID(groupID);
		setHomeworkID(homeworkID);
		setPublisherID(publisherID);
		setMarkingEffort(markingEffort);
		setActualMarkingCompletionDate(actualMarkingCompletionDate);
		setTargetMarkingCompletionDate(targetMarkingCompletionDate);
		setDueDate(dueDate);
		setDraft(isDraft);
		setGraded(isGraded);
	}

	public int getGroupHomeworkID() {
		return groupHomeworkID;
	}

	public void setGroupHomeworkID(int groupHomeworkID) {
		this.groupHomeworkID = groupHomeworkID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getHomeworkID() {
		return homeworkID;
	}

	public void setHomeworkID(int homeworkID) {
		this.homeworkID = homeworkID;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
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

}

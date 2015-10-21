package pie;

public class Subject {

	private int subjectID;
	private String subjectName;
	private String abbreviation;
	
	public Subject(int subjectID, String subjectName, String abbreviation) {
		setSubjectID(subjectID);
		setAbbreviation(abbreviation);
		setSubjectName(subjectName);
	}
	
	public int getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	
}

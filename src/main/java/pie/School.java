package pie;

import java.util.Date;

public class School {

	private int schoolID;
	private Address schoolAddress;
	private String schoolName;
	private Date schoolCreatedDate;
	private Date schoolLastUpdate;
	private String schoolCode;
	
	
	public int getSchoolID() {
		return schoolID;
	}
	
	public void setSchoolID(int schoolID) {
		this.schoolID = schoolID;
	}
	
	public Address getSchoolAddress() {
		return schoolAddress;
	}
	
	public void setSchoolAddress(Address schoolAddress) {
		this.schoolAddress = schoolAddress;
	}
	
	public String getSchoolName() {
		return schoolName;
	}
	
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	public Date getSchoolCreatedDate() {
		return schoolCreatedDate;
	}
	
	public void setSchoolCreatedDate(Date schoolCreatedDate) {
		this.schoolCreatedDate = schoolCreatedDate;
	}
	
	public Date getSchoolLastUpdate() {
		return schoolLastUpdate;
	}
	
	public void setSchoolLastUpdate(Date schoolLastUpdate) {
		this.schoolLastUpdate = schoolLastUpdate;
	}
	
	public String getSchoolCode() {
		return schoolCode;
	}
	
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	
	
	
}

package pie;

public class Country {

	private int countryID;
	private String countryName;
	private String countryPhoneCode;
	private String countryISO;

	public Country() {

	}

	public Country(int countryID, String countryName, String countryPhoneCode,
			String countryISO) {
		
		this.countryID = countryID;
		this.countryName = countryName;
		this.countryPhoneCode = countryPhoneCode;
		this.countryISO = countryISO;
	}

	public int getCountryID() {
		return countryID;
	}

	public void setCountryID(int countryID) {
		this.countryID = countryID;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryPhoneCode() {
		return countryPhoneCode;
	}

	public void setCountryPhoneCode(String countryPhoneCode) {
		this.countryPhoneCode = countryPhoneCode;
	}

	public String getCountryISO() {
		return countryISO;
	}

	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

}

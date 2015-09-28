package pie;

public class Address {

	private int addressID;
	private City addressCity;
	private String addressStreet;
	private String addressPostalCode;
	
	public Address() {
		
	}

	public Address(int addressID, City addressCity, String addressStreet,
			String addressPostalCode) {
		
		this.addressID = addressID;
		this.addressCity = addressCity;
		this.addressStreet = addressStreet;
		this.addressPostalCode = addressPostalCode;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public City getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(City addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

}
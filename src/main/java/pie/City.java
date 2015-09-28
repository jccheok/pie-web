package pie;

public class City {

	private int cityID;
	private Country cityCountry;
	private String cityName;

	public City() {

	}

	public City(int cityID, Country cityCountry, String cityName) {

		this.cityID = cityID;
		this.cityCountry = cityCountry;
		this.cityName = cityName;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public Country getCityCountry() {
		return cityCountry;
	}

	public void setCityCountry(Country cityCountry) {
		this.cityCountry = cityCountry;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}

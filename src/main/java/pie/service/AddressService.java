package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import pie.Address;
import pie.City;
import pie.Country;

public class AddressService extends DatabaseService {

	public AddressService(Connection conn) {
		super(conn);
	}

	public Address getAddress(int addressID) {
		Address address = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Address` WHERE addressID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, addressID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				address = new Address();
				address.setAddressID(addressID);
				address.setAddressStreet(resultSet.getString("addressStreet"));
				address.setAddressPostalCode(resultSet
						.getString("addressPostalCode"));
				address.setAddressCity(getCity(resultSet.getInt("cityID")));
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return address;
	}

	public City getCity(int cityID) {
		City city = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `City` WHERE cityID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, cityID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				city = new City();
				city.setCityID(cityID);
				city.setCityName(resultSet.getString("cityName"));
				city.setCityCountry(getCountry(resultSet.getInt("countryID")));
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return city;
	}
	
	public Country getCountry(int countryID) {
		Country country = null;

		try {

			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT * FROM `Country` WHERE countryID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, countryID);

			resultSet = pst.executeQuery();

			if (resultSet.next()) {
				country = new Country();
				country.setCountryID(countryID);
				country.setCountryName(resultSet.getString("countryName"));
				country.setCountryISO(resultSet.getString("countryISO"));
				country.setCountryPhoneCode(resultSet.getString("countryPhoneCode"));
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return country;
	}
}

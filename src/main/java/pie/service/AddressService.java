package pie.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Address;
import pie.City;
import pie.Country;
import pie.util.DatabaseConnector;

public class AddressService {

	public Address getAddress(int addressID) {
		Address address = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
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
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return address;
	}

	public City getCity(int cityID) {
		City city = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
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
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return city;
	}

	public Country getCountry(int countryID) {
		Country country = null;

		try {

			Connection conn = DatabaseConnector.getConnection();
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
				country.setCountryPhoneCode(resultSet
						.getString("countryPhoneCode"));
			}
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return country;
	}
}

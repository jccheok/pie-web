package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import pie.Address;
import pie.City;
import pie.Country;
import pie.utilities.DatabaseConnector;

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
				
				String addressStreet = resultSet.getString("addressStreet");
				String addressPostalCode = resultSet.getString("addressPostalCode");
				City addressCity = getCity(resultSet.getInt("cityID"));
				
				address = new Address(addressID, addressCity, addressStreet, addressPostalCode);
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
				
				String cityName = resultSet.getString("cityName");
				Country cityCountry = getCountry(resultSet.getInt("countryID"));
				
				city = new City(cityID, cityCountry, cityName);
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
				
				String countryName = resultSet.getString("countryName");
				String countryISO = resultSet.getString("countryISO");
				String countryPhoneCode = resultSet .getString("countryPhoneCode");
				
				country = new Country(countryID, countryName, countryPhoneCode, countryISO);
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return country;
	}

	public int registerAddress(String addressPostalCode, String addressStreet, City addressCity) {
		
		int addressID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Address` (cityID, addressStreet, addressPostalCode) VALUES (?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, addressCity.getCityID());
			pst.setString(2, addressStreet);
			pst.setString(3, addressPostalCode);
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if (resultSet.next()) {
				
				addressID = resultSet.getInt(1);
			}
			
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return addressID;
	}
}

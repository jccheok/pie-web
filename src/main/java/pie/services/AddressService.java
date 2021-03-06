package pie.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
				
				String addressStreet = resultSet.getString("streetName");
				String addressPostalCode = resultSet.getString("postalCode");
				City addressCity = getCity(resultSet.getInt("cityID"));
				
				address = new Address(addressID, addressCity, addressStreet, addressPostalCode);
			}
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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
				
				String cityName = resultSet.getString("name");
				Country cityCountry = getCountry(resultSet.getInt("countryID"));
				
				city = new City(cityID, cityCountry, cityName);
			}
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
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
				
				String countryName = resultSet.getString("name");
				String countryISO = resultSet.getString("ISO");
				String countryPhoneCode = resultSet .getString("phoneCode");
				
				country = new Country(countryID, countryName, countryPhoneCode, countryISO);
			}
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return country;
	}

	public int registerAddress(String addressPostalCode, String addressStreet, int cityID) {
		
		int addressID = -1;

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `Address` (cityID, streetName, postalCode) VALUES (?, ?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, cityID);
			pst.setString(2, addressStreet);
			pst.setString(3, addressPostalCode);
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if (resultSet.next()) {
				
				addressID = resultSet.getInt(1);
			}
			
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addressID;
	}
	
	public int registerCity(String cityName, int countryID){
		int cityID = -1;
		
		try{
			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "INSERT INTO `City` (countryID, name) VALUES (?, ?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, countryID);
			pst.setString(2, cityName);
			pst.executeUpdate();
			
			resultSet = pst.getGeneratedKeys();
			
			if(resultSet.next()){
				
				cityID = resultSet.getInt(1); 
			}
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return cityID;
	}
	
	public String getFullCityName(int cityID) {
		String fullCityName = null;
		
		City city = getCity(cityID);
		fullCityName = city.getCityName() + " - " + city.getCityCountry().getCountryName();
		
		return fullCityName;
	}

	public City[] getAllCities() {
		
		City[] cityList = {};

		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT cityID FROM `City`";
			pst = conn.prepareStatement(sql);

			resultSet = pst.executeQuery();

			List<City> tempCityList = new ArrayList<City>();
			while (resultSet.next()) {
				tempCityList.add(getCity(resultSet.getInt(1)));
			}
			cityList = tempCityList.toArray(cityList);

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cityList;
	}
	
	public int getAddressID(String addressPostalCode){
		int addressID = -1;
		try {

			Connection conn = DatabaseConnector.getConnection();
			PreparedStatement pst = null;
			ResultSet resultSet = null;

			String sql = "SELECT addressID FROM `Address` WHERE postalCode = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, addressPostalCode);
			resultSet = pst.executeQuery();

			if(resultSet.next()){
				addressID = resultSet.getInt("addressID");
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return addressID;
	}
	
}

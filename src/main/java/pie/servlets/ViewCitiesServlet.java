package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.City;
import pie.services.AddressService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewCitiesServlet extends HttpServlet {

	private static final long serialVersionUID = -8266524259375345729L;
	
	AddressService addressService;

	@Inject
	public ViewCitiesServlet(AddressService addressService) {
		this.addressService = addressService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		
		JSONArray cityList = new JSONArray();
		for (City city : addressService.getAllCities()) {
			
			int cityID = city.getCityID();
			
			JSONObject cityDetails = new JSONObject();
			cityDetails.put("cityID", cityID);
			cityDetails.put("cityFullName", addressService.getFullCityName(cityID));
			cityList.put(cityDetails);
		}
		responseObject.put("cityList", cityList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}

package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.SchoolRegistrationResult;
import pie.services.AddressService;
import pie.services.SchoolService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterSchoolServlet extends HttpServlet {

	private static final long serialVersionUID = -3878265942477329631L;

	SchoolService schoolService;
	AddressService addressService;

	@Inject
	public RegisterSchoolServlet(SchoolService schoolService, AddressService addressService) {
		this.schoolService = schoolService;
		this.addressService = addressService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String schoolName = null;
		String schoolCode = null;
		String addressStreet = null;
		int cityID = 0;
		String addressPostalCode = null; 
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "schoolName", "schoolCode",
					"addressStreet", "countryID", "cityName", "addressPostalCode");
			schoolName = requestParameters.get("schoolName");
			schoolCode = requestParameters.get("schoolCode");
			addressStreet = requestParameters.get("addressStreet");
			cityID = Integer.parseInt(requestParameters.get("cityID"));
			addressPostalCode = requestParameters.get("addressPostalCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		int addressID = addressService.registerAddress(addressPostalCode, addressStreet, cityID);

		SchoolRegistrationResult registrationResult = schoolService.registerSchool(schoolName, schoolCode, addressID);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}

package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.SchoolRegistrationResult;
import pie.services.SchoolService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterSchoolServlet extends HttpServlet {

	private static final long serialVersionUID = -3878265942477329631L;

	private SchoolService schoolService;

	@Inject
	public RegisterSchoolServlet(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String schoolName = request.getParameter("schoolName");
		String schoolCode = request.getParameter("schoolCode");
		String addressStreet = request.getParameter("addressStreet");
		int countryID = Integer.parseInt(request.getParameter("countryID"));
		String cityName = request.getParameter("cityName");
		String addressPostalCode = request.getParameter("addressPostalCode");

		SchoolRegistrationResult registrationResult = schoolService.registerSchool(schoolName, schoolCode, addressStreet, countryID, cityName, addressPostalCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}

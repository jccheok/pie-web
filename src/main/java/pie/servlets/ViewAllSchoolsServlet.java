package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.School;
import pie.services.SchoolService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewAllSchoolsServlet extends HttpServlet{

	private static final long serialVersionUID = 2511733009744406347L;

	SchoolService schoolService;

	@Inject
	public ViewAllSchoolsServlet(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		schoolService = new SchoolService();
		
		JSONObject responseObject = new JSONObject();
		
		JSONArray schoolList = new JSONArray();
		for (School school : schoolService.getAllSchools()) {
			
			int schoolID = school.getSchoolID();
			
			JSONObject schoolDetails = new JSONObject();
			schoolDetails.put("schoolID", schoolID);
			schoolDetails.put("schoolName",school.getSchoolName());
			schoolDetails.put("schoolAddressStreet", school.getSchoolAddress().getAddressStreet());
			schoolDetails.put("schoolPostalCode", school.getSchoolAddress().getAddressPostalCode());
			schoolDetails.put("schoolCountry", school.getSchoolAddress().getAddressCity().getCityCountry().getCountryName());
			schoolDetails.put("schoolCity", school.getSchoolAddress().getAddressCity().getCityName());
		
			schoolList.put(schoolDetails);
		}
		responseObject.put("schoolList", schoolList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
}

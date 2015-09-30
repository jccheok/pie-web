package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UserRegistrationResult;
import pie.services.ParentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterParentServlet extends HttpServlet {

	private static final long serialVersionUID = 6025770482395154507L;

	ParentService parentService;

	@Inject
	public RegisterParentServlet(ParentService parentService) {
		this.parentService = parentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userFirstName = null;
		String userLastName = null;
		String userEmail = null;
		String userPassword = null;
		String userMobile = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userFirstName", "userLastName", "userEmail", "userPassword", "userMobile");
			userFirstName = requestParameters.get("userFirstName");
			userLastName = requestParameters.get("userLastName");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			userMobile = requestParameters.get("userMobile");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		UserRegistrationResult registrationResult = parentService.registerParent(userFirstName, userLastName, userEmail, userPassword, userMobile);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		if (registrationResult == UserRegistrationResult.SUCCESS) {
			// send email
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}
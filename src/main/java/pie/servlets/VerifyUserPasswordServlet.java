package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class VerifyUserPasswordServlet {

	UserService userService;
	
	@Inject
	public VerifyUserPasswordServlet(UserService userService) {
		this.userService = userService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userEmail = null;
		String userPassword = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userEmail", "userPassword");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			
		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		boolean verifyResult = userService.credentialsMatch(userEmail, userPassword);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", verifyResult);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}

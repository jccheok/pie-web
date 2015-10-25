package pie.servlets.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.AuthService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetUserTokenServlet extends HttpServlet {

	private static final long serialVersionUID = 4661920460507518844L;
	
	UserService userService;
	AuthService authService;
	
	@Inject
	public GetUserTokenServlet(UserService userService, AuthService authService) {
		this.userService = userService;
		this.authService = authService;
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
		if(verifyResult){
			String authToken = authService.getAuthToken(userService.getUserID(userEmail));
			
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", authToken);

		}else{
			responseObject.put("result", "INVALID_PASSWORD");
			responseObject.put("message", "You entered the wrong password.");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}

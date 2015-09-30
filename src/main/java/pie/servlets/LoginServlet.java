package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.constants.LoginResult;
import pie.constants.SupportedPlatform;
import pie.services.AuthService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 483038783219697909L;
	
	UserService userService;
	
	@Inject
	public LoginServlet(UserService userService) {
		this.userService = userService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userEmail = null;
		String userPassword = null;
		int clientPlatformID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userEmail", "userPassword", "platformID");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			clientPlatformID = Integer.parseInt(requestParameters.get("platformID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
			
		SupportedPlatform clientPlatform = SupportedPlatform.getSupportedPlatform(clientPlatformID);

		LoginResult loginResult = userService.loginUser(userEmail, userPassword, clientPlatform);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", loginResult.toString());
		responseObject.put("message", loginResult.getDefaultMessage());
		
		if (loginResult == LoginResult.SUCCESS) {
			
			User user = userService.getUser(userService.getUserID(userEmail));
			
			JSONObject userJSON = new JSONObject();
			userJSON.put("userID", user.getUserID());
			userJSON.put("userFirstName", user.getUserFirstName());
			userJSON.put("userLastName", user.getUserLastName());
			userJSON.put("userType", user.getUserType().toString());
			userJSON.put("userEmail", user.getUserEmail());
			userJSON.put("userMobile", user.getUserMobile());
			
			HashMap<String,Object> claims = new HashMap<String,Object>();
			claims.put("userID", Integer.toString(user.getUserID()));
			claims.put("userFullName", user.getUserFullName());
			claims.put("userType", user.getUserType().toString());
			String token = AuthService.createToken("login", 86400000, claims);
			
			responseObject.put("user", userJSON);
			response.addHeader("X-Auth-Token", token);
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}
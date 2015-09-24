package pie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.service.UserService;
import pie.service.UserService.LoginResult;

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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		String userEmail = request.getParameter("userEmail");
		String userPassword = request.getParameter("userPassword");

		LoginResult loginResult = userService.loginUser(userEmail, userPassword);
		
		JSONObject responseObject = new JSONObject();
		
		for (LoginResult result : LoginResult.values()) {
			if (result == loginResult) {
				
				responseObject.put("result", result.toString());
				break;
			}
		}
		
		if (loginResult == LoginResult.SUCCESS) {
			
			User user = userService.getUser(userService.getUserID(userEmail));
			JSONObject userJSON = new JSONObject();
			userJSON.put("userID", user.getUserID());
			userJSON.put("userFirstName", user.getUserFirstName());
			userJSON.put("userLastName", user.getUserLastName());
			userJSON.put("userType", user.getUserType().name());
			userJSON.put("userEmail", user.getUserEmail());
			userJSON.put("userMobile", user.getUserMobile());
			
			responseObject.put("user", userJSON);
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RetrieveSecurityQuestionServlet extends HttpServlet {

	private static final long serialVersionUID = -7865633897590634094L;
	
	UserService userService;
	
	@Inject
	public RetrieveSecurityQuestionServlet(UserService userService) {
		this.userService = userService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userEmail = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userEmail");
			userEmail = requestParameters.get("userEmail");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		JSONObject responseObject = new JSONObject();
		User user = userService.getUser(userService.getUserID(userEmail));
		String securityQuestionDescription =  user.getUserSecurityQuestion().getSecurityQuestionDescription();
		
		if(userService.getUserID(userEmail) == -1){
			responseObject.put("result", "INVALID_EMAIL");
			responseObject.put("message", "The email you entered is wrong");
		}else if(securityQuestionDescription == null){
			responseObject.put("result", "QUESTION_NOT_SET");
			responseObject.put("message", "You did not set security question!");
		}else{
			responseObject.put("result", "Valid user");
			responseObject.put("message", securityQuestionDescription);
			responseObject.put("userID", user.getUserID());
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}


}

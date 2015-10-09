package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UpdatePasswordResult;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UpdatePasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 4626348048572650529L;

	UserService userService;
	
	@Inject
	public UpdatePasswordServlet(UserService userService) {
		this.userService = userService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;
		String newUserPassword = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userID", "newUserPassword");

			userID = Integer.parseInt(requestParameters.get("userID"));
			newUserPassword = requestParameters.get("newUserPassword");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		UpdatePasswordResult updatePasswordResult = userService.updatePassword(userID, newUserPassword);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", updatePasswordResult.toString());
		responseObject.put("message", updatePasswordResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
